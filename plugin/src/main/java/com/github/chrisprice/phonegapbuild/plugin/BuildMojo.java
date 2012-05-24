package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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
import com.github.chrisprice.phonegapbuild.api.data.me.MeAppResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.github.chrisprice.phonegapbuild.api.managers.MeManager;
import com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeys;
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
   * Application identifier file.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/app.id"
   * @readonly
   */
  private File appIdFile;

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
   * iOS signing key identifier
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/ios-key.id"
   * @readonly
   */
  private File iOsKeyIdFile;

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

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().debug("Creating zip for upload to cloud.");

    final File appSource = createUploadPackage();

    getLog().debug("Authenticating.");

    WebResource webResource = meManager.createRootWebResource(username, password);

    getLog().debug("Requesting summary from cloud.");

    MeResponse me = meManager.requestMe(webResource);

    getLog().debug("Checking for existing app.");

    MeAppResponse appSummary = getStoredAppSummary(me);

    AppResponse appDetails = null;
    if (appSummary != null) {

      getLog().info("Starting upload to existing app id " + appSummary.getId());

      appDetails = appsManager.putApp(webResource, appSummary.getResourcePath(), null, appSource);

    } else {

      getLog().debug("Checking for existing ios key.");

      HasResourceIdAndPath<Key> iOsKey = getStoredIOsKey(me);
      if (iOsKey == null && keys != null) {
        getLog().debug("Fetching keys dependencies");

        FetchKeys fetchKeys = new FetchKeys();
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

        storeIOsKey(iOsKey.getResourceId());
      }

      getLog().debug("Building upload request.");

      AppDetailsRequest appDetailsRequest = createNewAppUploadDetails(iOsKey.getResourceId());

      getLog().info("Starting upload.");

      appDetails = appsManager.postNewApp(webResource, me.getApps().getResourcePath(), appDetailsRequest, appSource);

      getLog().info("Storing new app id " + appDetails.getId());

      storeAppSummary(appDetails);
    }

    getLog().info("Starting downloads.");

    downloadArtifacts(webResource, appDetails);
  }

  private void storeIOsKey(ResourceId<Key> iOsKeyId) throws MojoExecutionException {
    try {
      FileUtils.writeStringToFile(iOsKeyIdFile, Integer.toString(iOsKeyId.getId()));
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to store iOS key id", e);
    }
  }

  private IOsKeyRequest createIOsKeyUploadRequest() {
    IOsKeyRequest iOsKeyRequest = new IOsKeyRequest();
    iOsKeyRequest.setTitle(appTitle);
    iOsKeyRequest.setPassword(iOsCertificatePassword);
    return iOsKeyRequest;
  }

  private HasResourceIdAndPath<Key> getStoredIOsKey(MeResponse meResponse) throws MojoExecutionException {
    try {
      if (!iOsKeyIdFile.exists()) {
        return null;
      }
      int keyId = Integer.parseInt(FileUtils.readFileToString(iOsKeyIdFile));
      MeKeyResponse[] all = meResponse.getKeys().getIos().getAll();
      for (MeKeyResponse key : all) {
        if (key.getId() == keyId) {
          return key;
        }
      }
      return null;
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to read stored iOS key id", e);
    }
  }

  private void storeAppSummary(AppResponse appDetails) throws MojoExecutionException {
    try {
      FileUtils.writeStringToFile(appIdFile, Integer.toString(appDetails.getId()));
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to store app id", e);
    }
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

  /**
   * Check if the stored app id (if it exists) is a known app and return it.
   */
  MeAppResponse getStoredAppSummary(MeResponse meResponse) throws MojoExecutionException {
    try {
      if (!appIdFile.exists()) {
        return null;
      }
      int appId = Integer.parseInt(FileUtils.readFileToString(appIdFile));
      MeAppResponse[] all = meResponse.getApps().getAll();
      for (MeAppResponse app : all) {
        if (app.getId() == appId) {
          return app;
        }
      }
      return null;
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to read stored app id", e);
    }
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

  public void setAppIdFile(File appIdFile) {
    this.appIdFile = appIdFile;
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

  public void setiOsKeyIdFile(File iOsKeyIdFile) {
    this.iOsKeyIdFile = iOsKeyIdFile;
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

}
