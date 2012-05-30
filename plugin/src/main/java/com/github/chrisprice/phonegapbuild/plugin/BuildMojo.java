package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.me.MePlatformResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.github.chrisprice.phonegapbuild.plugin.utils.AppDownloader;
import com.github.chrisprice.phonegapbuild.plugin.utils.AppUploadPackager;
import com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeys;
import com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeysImpl;
import com.github.chrisprice.phonegapbuild.plugin.utils.FileResourceIdStore;
import com.github.chrisprice.phonegapbuild.plugin.utils.ResourceIdStore;
import com.sun.jersey.api.client.WebResource;

/**
 * Compress the exploded package to a zip using the specified filters. Add in the config.xml from
 * src/main/phonegap-build/.
 * 
 * The compressed artifact will then be uploaded to either a pre-existing cloud app instance or a new one depending on
 * whether a stored app id can be found.
 * 
 * @goal build
 * @phase package
 * @requiresDependencyResolution compile
 */
public class BuildMojo extends AbstractPhoneGapBuildMojo {

  /**
   * The Zip archiver.
   * 
   * @component role="org.codehaus.plexus.archiver.Archiver" roleHint="zip"
   */
  private ZipArchiver zipArchiver;

  /**
   * The Zip un-archiver.
   * 
   * @component role="org.codehaus.plexus.archiver.UnArchiver" roleHint="zip"
   */
  private ZipUnArchiver zipUnArchiver;

  /**
   * @component
   */
  private MavenProjectHelper mavenProjectHelper;

  /**
   * @parameter default-value="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * Configuration file.
   * 
   * @parameter expression="${basedir}/src/main/phonegap-build/config.xml"
   */
  private File configFile;

  /**
   * Working directory.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build"
   * @readonly
   */
  private File workingDirectory;

  /**
   * iOS p12 certificate
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/ios.p12"
   */
  private File iOsCertificate;

  /**
   * iOS certificate password
   * 
   * @parameter expression="${phonegap-build.ios.certificate.password}"
   */
  private String iOsCertificatePassword;

  /**
   * iOS mobileprovision file
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/ios.mobileprovision"
   */
  private File iOsMobileProvision;

  /**
   * A comma delimited string of artifact co-ordinates used to filter the dependencies list for key packages. The
   * co-ordinates should be of the form groupId:artifactId.
   * 
   * @parameter
   */
  private String keys;

  /**
   * The application title, defaults to the final name of the app but will be overridden by any title specified in the
   * config file.
   * 
   * @parameter expression="${project.build.finalName}"
   */
  private String appTitle;

  /**
   * Exploded WAR directory
   * 
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   */
  private File warDirectory;

  /**
   * A set of file patterns to include in the zip.
   * 
   * @parameter
   */
  private String[] warIncludes;

  /**
   * A set of file patterns to exclude from the zip.
   * 
   * Defaults to "WEB-INF&#47;**&#47;*", "WEB-INF"
   * 
   * @parameter
   */
  private String[] warExcludes = new String[] {"WEB-INF/**/*", "WEB-INF"};

  /**
   * The platforms to build for.
   * 
   * Defaults to android, blackberry, ios, symbian, webos and winphone
   * 
   * @parameter
   */
  private String[] platforms = new String[] {"android", "blackberry", "ios", "symbian", "webos", "winphone"};

  private FetchKeys fetchKeys = new FetchKeysImpl();
  private ResourceIdStore<App> appIdStore = new FileResourceIdStore<App>();
  private ResourceIdStore<Key> keyIdStore = new FileResourceIdStore<Key>();
  private AppUploadPackager appUploadPackager = new AppUploadPackager();
  private AppDownloader appDownloader = new AppDownloader();

  public void execute() throws MojoExecutionException, MojoFailureException {
    ensureWorkingDirectory();

    getLog().debug("Creating zip for upload to cloud.");

    appUploadPackager.setConfigFile(configFile);
    appUploadPackager.setWarDirectory(warDirectory);
    appUploadPackager.setWarExcludes(warExcludes);
    appUploadPackager.setWarIncludes(warIncludes);
    appUploadPackager.setWorkingDirectory(workingDirectory);
    appUploadPackager.setZipArchiver(zipArchiver);
    File appSource = appUploadPackager.createUploadPackage();

    getLog().debug("Authenticating.");
    WebResource webResource = getRootWebResource();

    getLog().debug("Requesting summary from cloud.");
    MeResponse me = meManager.requestMe(webResource);

    getLog().debug("Checking for existing app.");
    appIdStore.setAlias("app");
    appIdStore.setWorkingDirectory(workingDirectory);
    HasResourceIdAndPath<App> appSummary = appIdStore.load(me.getApps().getAll());

    if (appSummary == null) {
      ResourceId<Key> iOsKeyId = null;

      getLog().debug("Ensuring ios key exists if it is a target platform.");
      if (iOsIsATargetPlatform()) {
        MePlatformResponse iosKeys = me.getKeys().getIos();
        iOsKeyId = ensureIOsKey(webResource, iosKeys.getResourcePath(), iosKeys.getAll());
      }

      getLog().debug("Building upload request.");
      AppDetailsRequest appDetailsRequest = createAppDetailsRequest(iOsKeyId);

      getLog().info("Starting upload.");
      appSummary = appsManager.postNewApp(webResource, me.getApps().getResourcePath(), appDetailsRequest, appSource);

      getLog().info("Storing new app id " + appSummary.getResourceId());
      appIdStore.save(appSummary.getResourceId());
    } else {
      getLog().info("Starting upload to existing app id " + appSummary.getResourceId());
      appsManager.putApp(webResource, appSummary.getResourcePath(), null, appSource);
    }

    getLog().info("Starting downloads.");
    appDownloader.setAppsManager(appsManager);
    appDownloader.setMavenProjectHelper(mavenProjectHelper);
    appDownloader.setProject(project);
    appDownloader.setWorkingDirectory(workingDirectory);
    appDownloader.downloadArtifacts(webResource, appSummary.getResourcePath(), Platform.get(platforms));
  }

  private void ensureWorkingDirectory() {
    if (!workingDirectory.exists()) {
      if (!workingDirectory.mkdirs()) {
        throw new RuntimeException("Could not create working directory at " + workingDirectory.getAbsolutePath() + ".");
      }
    } else {
      if (!workingDirectory.isDirectory()) {
        throw new RuntimeException("Working directory is not a directory " + workingDirectory.getAbsolutePath() + ".");
      }
    }
  }

  private boolean iOsIsATargetPlatform() {
    return Arrays.asList(platforms).contains(Platform.IOS.getValue());
  }

  ResourceId<Key> ensureIOsKey(WebResource webResource, ResourcePath<PlatformKeys> keysResource,
      HasResourceIdAndPath<Key>[] keyResources) throws MojoExecutionException, MojoFailureException {
    getLog().debug("Checking for existing ios key.");
    keyIdStore.setAlias("ios-key");
    keyIdStore.setWorkingDirectory(workingDirectory);
    HasResourceIdAndPath<Key> iOsKey = keyIdStore.load(keyResources);

    if (iOsKey != null) {
      return iOsKey.getResourceId();
    }

    if (keys != null) {
      getLog().debug("Fetching keys dependencies");
      fetchKeys.setIncludes(keys);
      fetchKeys.setProject(project);
      fetchKeys.setTargetDirectory(workingDirectory);
      fetchKeys.setZipUnArchiver(zipUnArchiver);
      fetchKeys.execute();
    }

    if (iOsCertificate == null || !iOsCertificate.exists()) {
      String path = iOsCertificate == null ? null : iOsCertificate.getAbsolutePath();
      throw new MojoFailureException("iOsCertificate does not exist " + path + ".");
    }

    if (iOsMobileProvision == null || !iOsMobileProvision.exists()) {
      String path = iOsMobileProvision == null ? null : iOsMobileProvision.getAbsolutePath();
      throw new MojoFailureException("iOsMobileProvision does not exist " + path + ".");
    }

    if (iOsCertificatePassword == null || iOsCertificatePassword.isEmpty()) {
      throw new MojoFailureException("iOsCertificatePassword not defined or blank.");
    }

    getLog().debug("Building iOS key upload request.");
    IOsKeyRequest iOsKeyRequest = createIOsKeyUploadRequest();

    getLog().debug("iOS key not found, uploading.");
    iOsKey = keysManager.postNewKey(webResource, keysResource, iOsKeyRequest, iOsCertificate, iOsMobileProvision);

    getLog().info("Storing new iOS key id " + iOsKey.getResourceId());
    keyIdStore.save(iOsKey.getResourceId());

    return iOsKey.getResourceId();
  }

  private IOsKeyRequest createIOsKeyUploadRequest() {
    IOsKeyRequest iOsKeyRequest = new IOsKeyRequest();
    iOsKeyRequest.setTitle(appTitle);
    iOsKeyRequest.setPassword(iOsCertificatePassword);
    return iOsKeyRequest;
  }

  private AppDetailsRequest createAppDetailsRequest(ResourceId<Key> iOsKeyId) {
    AppDetailsRequest appDetailsRequest = new AppDetailsRequest();
    appDetailsRequest.setCreateMethod("file");
    appDetailsRequest.setTitle(appTitle);
    AppDetailsRequest.Keys keys = new AppDetailsRequest.Keys();
    if (iOsKeyId != null) {
      keys.setIos(iOsKeyId.getId());
    }
    appDetailsRequest.setKeys(keys);
    return appDetailsRequest;
  }


  public void setZipArchiver(ZipArchiver zipArchiver) {
    this.zipArchiver = zipArchiver;
  }

  public void setMavenProjectHelper(MavenProjectHelper mavenProjectHelper) {
    this.mavenProjectHelper = mavenProjectHelper;
  }

  public void setProject(MavenProject project) {
    this.project = project;
  }

  public void setConfigFile(File configFile) {
    this.configFile = configFile;
  }

  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public void setAppTitle(String appTitle) {
    this.appTitle = appTitle;
  }

  public void setWarDirectory(File warDirectory) {
    this.warDirectory = warDirectory;
  }

  public void setWarIncludes(String[] includes) {
    this.warIncludes = includes;
  }

  public void setWarExcludes(String[] excludes) {
    this.warExcludes = excludes;
  }

  public void setZipUnArchiver(ZipUnArchiver zipUnArchiver) {
    this.zipUnArchiver = zipUnArchiver;
  }

  public void setiOsCertificate(File iOsCertificate) {
    this.iOsCertificate = iOsCertificate;
  }

  public void setiOsCertificatePassword(String iOsCertificatePassword) {
    this.iOsCertificatePassword = iOsCertificatePassword;
  }

  public void setiOsMobileProvision(File iOsMobileProvision) {
    this.iOsMobileProvision = iOsMobileProvision;
  }

  public void setKeys(String keys) {
    this.keys = keys;
  }

  public void setPlatforms(String[] platforms) {
    this.platforms = platforms;
  }

  public void setFetchKeys(FetchKeys fetchKeys) {
    this.fetchKeys = fetchKeys;
  }

  public void setAppIdStore(ResourceIdStore<App> appIdStore) {
    this.appIdStore = appIdStore;
  }

  public void setKeyIdStore(ResourceIdStore<Key> keyIdStore) {
    this.keyIdStore = keyIdStore;
  }

}
