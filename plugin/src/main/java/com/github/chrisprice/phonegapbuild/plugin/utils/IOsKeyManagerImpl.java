package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.sun.jersey.api.client.WebResource;

@Component(role = IOsKeyManager.class)
public class IOsKeyManagerImpl implements IOsKeyManager {

  @Requirement
  private ResourceIdStore<Key> keyIdStore;
  @Requirement
  private FetchKeys fetchKeys;
  @Requirement
  private WagonManager wagonManager;
  private KeysManager keysManager;
  private Log log;

  /**
   * iOS key id (used in preference to uploading an ios key)
   */
  private Integer iOsKeyId;

  /**
   * iOS certificate server id (used in preference to iOsCertificate*)
   */
  private String iOsServer;

  /**
   * iOS p12 certificate. Deprecated - use iOsServer instead.
   */
  private File iOsCertificate;

  /**
   * iOS certificate password. Deprecated - use iOsServer instead.
   */
  private String iOsCertificatePassword;

  /**
   * iOS mobileprovision file
   */
  private File iOsMobileProvision;

  /**
   * A comma delimited string of artifact co-ordinates used to filter the dependencies list for key
   * packages. The co-ordinates should be of the form groupId:artifactId.
   */
  private String keys;

  private MavenProject project;
  private File workingDirectory;
  private String appTitle;

  @Override
  public ResourceId<Key> ensureIOsKey(WebResource webResource,
      ResourcePath<PlatformKeys> keysResource, HasResourceIdAndPath<Key>[] keyResources)
      throws MojoExecutionException, MojoFailureException {
    getLog().debug("Checking for existing ios key.");
    keyIdStore.setAlias("ios-key");
    keyIdStore.setWorkingDirectory(workingDirectory);
    keyIdStore.setIdOverride(iOsKeyId);
    HasResourceIdAndPath<Key> iOsKey = keyIdStore.load(keyResources);

    if (iOsKey != null) {
      return iOsKey.getResourceId();
    }

    if (keys != null) {
      getLog().debug("Fetching keys dependencies");
      fetchKeys.setIncludes(keys);
      fetchKeys.setProject(project);
      fetchKeys.setTargetDirectory(workingDirectory);
      fetchKeys.execute();
    }

    File iOsCertificate;
    String iOsCertificatePassword;

    if (iOsServer != null) {
      AuthenticationInfo info = wagonManager.getAuthenticationInfo(iOsServer);
      if (info == null) {
        throw new RuntimeException("Server not found in settings.xml " + iOsServer + ".");
      }
      if (info.getPrivateKey() == null) {
        throw new RuntimeException("No private key found for server " + iOsServer + ".");
      }
      iOsCertificate = new File(info.getPrivateKey());
      if (info.getPassphrase() == null) {
        throw new RuntimeException("No passphrase found for server " + iOsServer + ".");
      }
      iOsCertificatePassword = info.getPassphrase();
    } else {
      getLog().warn(
          "iOsServer not specified, falling back to iOsCertificate/iOsCertificatePassword.");
      iOsCertificate = this.iOsCertificate;
      iOsCertificatePassword = this.iOsCertificatePassword;
    }

    if (iOsCertificate == null || !iOsCertificate.exists()) {
      String path = iOsCertificate == null ? null : iOsCertificate.getAbsolutePath();
      throw new MojoFailureException("ios certificate does not exist " + path + ".");
    }

    if (iOsCertificatePassword == null || iOsCertificatePassword.isEmpty()) {
      throw new MojoFailureException("ios certificate password not defined or blank.");
    }

    if (iOsMobileProvision == null || !iOsMobileProvision.exists()) {
      String path = iOsMobileProvision == null ? null : iOsMobileProvision.getAbsolutePath();
      throw new MojoFailureException("ios mobileprovision does not exist " + path + ".");
    }

    getLog().debug("Building iOS key upload request.");
    IOsKeyRequest iOsKeyRequest = createIOsKeyUploadRequest(appTitle, iOsCertificatePassword);

    getLog().debug("iOS key not found, uploading.");
    iOsKey =
        keysManager.postNewKey(webResource, keysResource, iOsKeyRequest, iOsCertificate,
            iOsMobileProvision);

    getLog().info("Storing new iOS key id " + iOsKey.getResourceId());
    keyIdStore.save(iOsKey.getResourceId());

    return iOsKey.getResourceId();
  }

  private IOsKeyRequest createIOsKeyUploadRequest(String appTitle, String iOsCertificatePassword) {
    IOsKeyRequest iOsKeyRequest = new IOsKeyRequest();
    iOsKeyRequest.setTitle(appTitle);
    iOsKeyRequest.setPassword(iOsCertificatePassword);
    return iOsKeyRequest;
  }

  @Override
  public void setLog(Log log) {
    this.log = log;
  }

  private Log getLog() {
    return log;
  }

  public void setKeyIdStore(ResourceIdStore<Key> keyIdStore) {
    this.keyIdStore = keyIdStore;
  }

  public void setFetchKeys(FetchKeys fetchKeys) {
    this.fetchKeys = fetchKeys;
  }

  @Override
  public void setiOsKeyId(Integer iOsKeyId) {
    this.iOsKeyId = iOsKeyId;
  }

  @Override
  public void setiOsServer(String iOsServer) {
    this.iOsServer = iOsServer;
  }

  @Override
  public void setiOsCertificate(File iOsCertificate) {
    this.iOsCertificate = iOsCertificate;
  }

  @Override
  public void setiOsCertificatePassword(String iOsCertificatePassword) {
    this.iOsCertificatePassword = iOsCertificatePassword;
  }

  @Override
  public void setiOsMobileProvision(File iOsMobileProvision) {
    this.iOsMobileProvision = iOsMobileProvision;
  }

  @Override
  public void setKeys(String keys) {
    this.keys = keys;
  }

  @Override
  public void setProject(MavenProject project) {
    this.project = project;
  }

  public void setKeysManager(KeysManager keysManager) {
    this.keysManager = keysManager;
  }

  @Override
  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  @Override
  public void setAppTitle(String appTitle) {
    this.appTitle = appTitle;
  }

  public void setWagonManager(WagonManager wagonManager) {
    this.wagonManager = wagonManager;
  }

}
