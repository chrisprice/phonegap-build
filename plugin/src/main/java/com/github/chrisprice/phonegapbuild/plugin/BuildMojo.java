package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.github.chrisprice.phonegapbuild.api.managers.MeManager;
import com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeys;
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
public class BuildMojo extends AbstractMojo {

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
   * PhoneGap Build username
   * 
   * @parameter expression="${phonegap-build.username}"
   */
  private String username;

  /**
   * PhoneGap Build password
   * 
   * @parameter expression="${phonegap-build.password}"
   */
  private String password;

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
   * The application title. Can also be overridden in the config file.
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
  private String[] includes;

  /**
   * A set of file patterns to exclude from the zip.
   * 
   * Defaults to "WEB-INF&#47;**&#47;*", "WEB-INF"
   * 
   * @parameter
   */
  private String[] excludes = new String[] {"WEB-INF/**/*", "WEB-INF"};

  /**
   * The platforms to build for.
   * 
   * Defaults to android, blackberry, ios, symbian, webos and winphone
   * 
   * @parameter
   */
  private String[] platforms = new String[] {"android", "blackberry", "ios", "symbian", "webos", "winphone"};

  private AppsManager appsManager = new AppsManager();
  private MeManager meManager = new MeManager();
  private KeysManager keysManager = new KeysManager();
  private FetchKeys fetchKeys = new FetchKeys();
  private ResourceIdStore<App> appIdStore = new FileResourceIdStore<App>();
  private ResourceIdStore<Key> keyIdStore = new FileResourceIdStore<Key>();

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().debug("Creating zip for upload to cloud.");

    final File appSource = createUploadPackage();

    getLog().debug("Authenticating.");

    WebResource webResource = meManager.createRootWebResource(username, password);

    getLog().debug("Requesting summary from cloud.");

    MeResponse me = meManager.requestMe(webResource);

    getLog().debug("Checking for existing app.");

    appIdStore.setAlias("app");
    appIdStore.setWorkingDirectory(workingDirectory);
    HasResourceIdAndPath<App> appSummary = appIdStore.load(me.getApps().getAll());

    AppResponse appDetails = null;
    if (appSummary != null) {

      getLog().info("Starting upload to existing app id " + appSummary.getResourceId());

      appDetails = appsManager.putApp(webResource, appSummary.getResourcePath(), null, appSource);

    } else {

      getLog().debug("Checking for existing ios key.");

      keyIdStore.setAlias("ios-key");
      keyIdStore.setWorkingDirectory(workingDirectory);

      HasResourceIdAndPath<Key> iOsKey = keyIdStore.load(me.getKeys().getIos().getAll());
      if (iOsKey == null && keys != null) {
        getLog().debug("Fetching keys dependencies");

        fetchKeys.setIncludes(keys);
        fetchKeys.setProject(project);
        fetchKeys.setTargetDirectory(workingDirectory);
        fetchKeys.setZipUnArchiver(zipUnArchiver);
        fetchKeys.execute();
      }

      if (iOsKey == null) {
        getLog().debug("Building iOS key upload request.");

        IOsKeyRequest iOsKeyRequest = createIOsKeyUploadRequest();

        getLog().debug("iOS key not found, uploading.");

        iOsKey =
            keysManager.postNewKey(webResource, me.getKeys().getIos().getResourcePath(), iOsKeyRequest, iOsCertificate,
                iOsMobileProvision);

        getLog().info("Storing new iOS key id " + iOsKey.getResourceId());

        keyIdStore.save(iOsKey.getResourceId());
      }

      getLog().debug("Building upload request.");

      AppDetailsRequest appDetailsRequest = createNewAppUploadDetails(iOsKey.getResourceId());

      getLog().info("Starting upload.");

      appDetails = appsManager.postNewApp(webResource, me.getApps().getResourcePath(), appDetailsRequest, appSource);

      getLog().info("Storing new app id " + appDetails.getResourceId());

      appIdStore.save(appDetails.getResourceId());
    }

    getLog().info("Starting downloads.");

    downloadArtifacts(webResource, appDetails);
  }

  private IOsKeyRequest createIOsKeyUploadRequest() {
    IOsKeyRequest iOsKeyRequest = new IOsKeyRequest();
    iOsKeyRequest.setTitle(appTitle);
    iOsKeyRequest.setPassword(iOsCertificatePassword);
    return iOsKeyRequest;
  }

  private void downloadArtifacts(WebResource webResource, AppResponse appDetails) throws MojoFailureException {
    for (String value : this.platforms) {
      Platform platform = Platform.get(value);
      if (platform == null) {
        throw new MojoFailureException("Unknown platform specified " + value);
      }
      getLog().info("Downloading binary for " + platform.getValue());
      // download the app
      File app = appsManager.downloadApp(webResource, appDetails.getResourcePath(), platform, workingDirectory);
      // attach it to the project with the appropriate classifier (platform) and type (extension)
      mavenProjectHelper.attachArtifact(project, FilenameUtils.getExtension(app.getName()), platform.getValue(), app);
    }
  }

  private AppDetailsRequest createNewAppUploadDetails(ResourceId<Key> iOsKeyId) {
    AppDetailsRequest appDetailsRequest = new AppDetailsRequest();
    appDetailsRequest.setCreateMethod("file");
    appDetailsRequest.setTitle(appTitle);
    if (iOsKeyId != null) {
      AppDetailsRequest.Keys keys = new AppDetailsRequest.Keys();
      keys.setIos(iOsKeyId.getId());
      appDetailsRequest.setKeys(keys);
    }
    return appDetailsRequest;
  }

  private File createUploadPackage() throws MojoExecutionException {
    workingDirectory.mkdirs();

    File file = new File(workingDirectory, "file.zip");

    try {
      zipArchiver.addDirectory(warDirectory, includes, excludes);
      zipArchiver.addFile(configFile, "config.xml");
      zipArchiver.setDestFile(file);
      zipArchiver.createArchive();
    } catch (Exception e) {
      throw new MojoExecutionException("Could not zip", e);
    }
    return file;
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

  public void setIncludes(String[] includes) {
    this.includes = includes;
  }

  public void setExcludes(String[] excludes) {
    this.excludes = excludes;
  }

  public void setAppsManager(AppsManager appsManager) {
    this.appsManager = appsManager;
  }

  public void setZipUnArchiver(ZipUnArchiver zipUnArchiver) {
    this.zipUnArchiver = zipUnArchiver;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public void setMeManager(MeManager meManager) {
    this.meManager = meManager;
  }

  public void setKeysManager(KeysManager keysManager) {
    this.keysManager = keysManager;
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
