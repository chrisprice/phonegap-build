package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest;
import com.github.chrisprice.phonegapbuild.api.data.me.MeKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MePlatformResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.github.chrisprice.phonegapbuild.plugin.utils.AndroidKeyManager;
import com.github.chrisprice.phonegapbuild.plugin.utils.AppDownloader;
import com.github.chrisprice.phonegapbuild.plugin.utils.AppUploadPackager;
import com.github.chrisprice.phonegapbuild.plugin.utils.IOsKeyManager;
import com.github.chrisprice.phonegapbuild.plugin.utils.ResourceIdStore;
import com.sun.jersey.api.client.WebResource;

/**
 * Compress the exploded package to a zip using the specified filters. Add in the config.xml from
 * src/main/phonegap-build/.
 * 
 * The compressed artifact will then be uploaded to either a pre-existing cloud app instance or a
 * new one depending on whether a stored app id can be found.
 * 
 * @goal build
 * @phase package
 * @requiresDependencyResolution compile
 */
public class BuildMojo extends AbstractPhoneGapBuildMojo {

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
   * App id (used in preference to creating a new app)
   * 
   * @parameter
   */
  private Integer appId;

  /**
   * iOS key id (used in preference to uploading an ios key)
   * 
   * @parameter
   */
  private Integer iOsKeyId;

  /**
   * iOS certificate server id (used in preference to iOsCertificate*)
   * 
   * @parameter expression="${phonegap-build.ios.server}"
   */
  private String iOsServer;

  /**
   * iOS p12 certificate. Deprecated - use iOsServer instead.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/ios.p12"
   * @deprecated
   */
  private File iOsCertificate;

  /**
   * iOS certificate password. Deprecated - use iOsServer instead.
   * 
   * @parameter expression="${phonegap-build.ios.certificate.password}"
   * @deprecated
   */
  private String iOsCertificatePassword;

  /**
   * iOS mobileprovision file
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/ios.mobileprovision"
   */
  private File iOsMobileProvision;

  /**
   * Enable android signing.
   * 
   * @parameter default-value="false"
   */
  private boolean androidSign;

  /**
   * Android key id (used in preference to uploading an android key)
   * 
   * @parameter
   */
  private Integer androidKeyId;

  /**
   * Android certificate server id (used in preference to androidCertificate*)
   * 
   * @parameter expression="${phonegap-build.android.server}"
   */
  private String androidServer;

  /**
   * Android certificate password. Deprecated - use androidServer instead.
   * 
   * @parameter expression="${phonegap-build.android.certificate.password}"
   * @deprecated
   */
  private String androidCertificatePassword;

  /**
   * Android keystore. Deprecated - use androidServer instead.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/android.keystore"
   * @deprecated
   */
  private File androidKeystore;

  /**
   * Android keystore password. Deprecated - use androidServer instead.
   * 
   * @parameter expression="${phonegap-build.android.keystore.password}"
   * @deprecated
   */
  private String androidKeystorePassword;

  /**
   * Android keystore certificate alias. Deprecated - use androidServer instead.
   * 
   * @parameter expression="${phonegap-build.android.certificate.alias}"
   * @deprecated
   */
  private String androidCertificateAlias;

  /**
   * A comma delimited string of artifact co-ordinates used to filter the dependencies list for key
   * packages. The co-ordinates should be of the form groupId:artifactId.
   * 
   * @parameter
   */
  private String keys;

  /**
   * The application title, defaults to the final name of the app but will be overridden by any
   * title specified in the config file.
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
  private String[] platforms = new String[] {
      "android", "blackberry", "ios", "symbian", "webos", "winphone"};

  /**
   * @component role="com.github.chrisprice.phonegapbuild.plugin.utils.ResourceIdStore"
   */
  private ResourceIdStore<App> appIdStore;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.plugin.utils.AppUploadPackager"
   */
  private AppUploadPackager appUploadPackager;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.plugin.utils.AppDownloader"
   */
  private AppDownloader appDownloader;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.plugin.utils.IOsKeyManager"
   */
  private IOsKeyManager iOsKeyManager;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.plugin.utils.AndroidKeyManager"
   */
  private AndroidKeyManager androidKeyManager;

  public void execute() throws MojoExecutionException, MojoFailureException {
    ensureWorkingDirectory();

    getLog().debug("Creating zip for upload to cloud.");

    appUploadPackager.setConfigFile(configFile);
    appUploadPackager.setWarDirectory(warDirectory);
    appUploadPackager.setWarExcludes(warExcludes);
    appUploadPackager.setWarIncludes(warIncludes);
    appUploadPackager.setWorkingDirectory(workingDirectory);
    File appSource = appUploadPackager.createUploadPackage();

    getLog().debug("Authenticating.");
    WebResource webResource = getRootWebResource();

    getLog().debug("Requesting summary from cloud.");
    MeResponse me = meManager.requestMe(webResource);

    getLog().debug("Checking for existing app.");
    appIdStore.setAlias("app");
    appIdStore.setWorkingDirectory(workingDirectory);
    appIdStore.setIdOverride(this.appId);
    HasResourceIdAndPath<App> appSummary = appIdStore.load(me.getApps().getAll());

    if (appSummary == null) {
      ResourceId<Key> computedIOsKeyId = null;
      ResourceId<Key> computedAndroidKeyId = null;

      getLog().debug("Ensuring ios key exists if it is a target platform.");
      if (targetPlatformsContains(Platform.IOS)) {
        MePlatformResponse iosKeys = me.getKeys().getIos();
        computedIOsKeyId = ensureIOsKey(webResource, iosKeys.getResourcePath(), iosKeys.getAll());
      }

      getLog().debug("Ensuring android key exists if it is a target platform and Android signing is enabled.");
      if (targetPlatformsContains(Platform.ANDROID) && androidSign) {
        MePlatformResponse androidKeys = me.getKeys().getAndroid();
        computedAndroidKeyId = ensureAndroidKey(webResource, androidKeys.getResourcePath(), androidKeys.getAll());
      }

      getLog().debug("Building upload request.");
      AppDetailsRequest appDetailsRequest = createAppDetailsRequest(computedIOsKeyId, computedAndroidKeyId);

      getLog().info("Starting upload.");
      appSummary =
          appsManager.postNewApp(webResource, me.getApps().getResourcePath(), appDetailsRequest,
              appSource);

      getLog().info("Storing new app id " + appSummary.getResourceId());
      appIdStore.save(appSummary.getResourceId());
    } else {
      getLog().info("Starting upload to existing app id " + appSummary.getResourceId());
      appsManager.putApp(webResource, appSummary.getResourcePath(), null, appSource);
    }

    getLog().info("Starting downloads.");
    appDownloader.setProject(project);
    appDownloader.setWorkingDirectory(workingDirectory);
    appDownloader.downloadArtifacts(webResource, appSummary.getResourcePath(), Platform
        .get(platforms));
  }

  private ResourceId<Key> ensureAndroidKey(WebResource webResource, ResourcePath<PlatformKeys> resourcePath,
      MeKeyResponse[] all) throws MojoFailureException {
    androidKeyManager.setAndroidCertificateAlias(androidCertificateAlias);
    androidKeyManager.setAndroidCertificatePassword(androidCertificatePassword);
    androidKeyManager.setAndroidKeyId(androidKeyId);
    androidKeyManager.setAndroidKeystore(androidKeystore);
    androidKeyManager.setAndroidKeystorePassword(androidKeystorePassword);
    androidKeyManager.setAndroidServer(androidServer);
    androidKeyManager.setAppTitle(appTitle);
    androidKeyManager.setLog(getLog());
    androidKeyManager.setWorkingDirectory(workingDirectory);
    return androidKeyManager.ensureAndroidKey(webResource, resourcePath, all);
  }

  private ResourceId<Key> ensureIOsKey(WebResource webResource,
      ResourcePath<PlatformKeys> resourcePath, MeKeyResponse[] all) throws MojoExecutionException,
      MojoFailureException {
    iOsKeyManager.setAppTitle(appTitle);
    iOsKeyManager.setKeys(keys);
    iOsKeyManager.setiOsCertificate(iOsCertificate);
    iOsKeyManager.setiOsCertificatePassword(iOsCertificatePassword);
    iOsKeyManager.setiOsKeyId(iOsKeyId);
    iOsKeyManager.setiOsMobileProvision(iOsMobileProvision);
    iOsKeyManager.setiOsServer(iOsServer);
    iOsKeyManager.setLog(getLog());
    iOsKeyManager.setProject(project);
    iOsKeyManager.setWorkingDirectory(workingDirectory);
    return iOsKeyManager.ensureIOsKey(webResource, resourcePath, all);
  }

  private void ensureWorkingDirectory() {
    if (!workingDirectory.exists()) {
      if (!workingDirectory.mkdirs()) {
        throw new RuntimeException("Could not create working directory at "
            + workingDirectory.getAbsolutePath() + ".");
      }
    } else {
      if (!workingDirectory.isDirectory()) {
        throw new RuntimeException("Working directory is not a directory "
            + workingDirectory.getAbsolutePath() + ".");
      }
    }
  }

  private boolean targetPlatformsContains(Platform platform) {
    return Arrays.asList(platforms).contains(platform.getValue());
  }

  private AppDetailsRequest createAppDetailsRequest(ResourceId<Key> iOsKeyId, ResourceId<Key> androidKeyId) {
    AppDetailsRequest appDetailsRequest = new AppDetailsRequest();
    appDetailsRequest.setCreateMethod("file");
    appDetailsRequest.setTitle(appTitle);
    AppDetailsRequest.Keys keys = new AppDetailsRequest.Keys();
    if (iOsKeyId != null) {
      keys.setIos(iOsKeyId.getId());
    }
    if (androidKeyId != null) {
      keys.setAndroid(androidKeyId.getId());
    }
    appDetailsRequest.setKeys(keys);
    return appDetailsRequest;
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

  public void setAppIdStore(ResourceIdStore<App> appIdStore) {
    this.appIdStore = appIdStore;
  }

  public void setiOsKeyManager(IOsKeyManager iOsKeyManager) {
    this.iOsKeyManager = iOsKeyManager;
  }

}
