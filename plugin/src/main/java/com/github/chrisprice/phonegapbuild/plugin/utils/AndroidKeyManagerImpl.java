package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.keys.AndroidKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.AndroidKeyUnlockRequest;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.sun.jersey.api.client.WebResource;

@Component(role = AndroidKeyManager.class)
public class AndroidKeyManagerImpl implements AndroidKeyManager {

  /**
   * Android key id (used in preference to uploading an android key)
   */
  private Integer androidKeyId;

  /**
   * Android certificate server id (used in preference to androidCertificate*)
   */
  private String androidServer;

  /**
   * Android certificate password. Deprecated - use androidServer instead.
   */
  private String androidCertificatePassword;

  /**
   * Android keystore. Deprecated - use androidServer instead.
   */
  private File androidKeystore;

  /**
   * Android keystore password. Deprecated - use androidServer instead.
   */
  private String androidKeystorePassword;

  /**
   * Android keystore certificate alias. Deprecated - use androidServer instead.
   */
  private String androidCertificateAlias;

  @Requirement
  private WagonManager wagonManager;

  @Requirement
  private ResourceIdStore<Key> keyIdStore;

  @Requirement
  private KeysManager keysManager;

  private Log log;
  private File workingDirectory;
  private String appTitle;

  @Override
  public ResourceId<Key> ensureAndroidKey(WebResource webResource, ResourcePath<PlatformKeys> keysResource,
      HasResourceIdAndPath<Key>[] keyResources) throws MojoFailureException {
    // most of this code is common with ioskeymanager
    getLog().debug("Checking for existing android key.");
    keyIdStore.setAlias("android-key");
    keyIdStore.setWorkingDirectory(workingDirectory);
    keyIdStore.setIdOverride(androidKeyId);
    HasResourceIdAndPath<Key> key = keyIdStore.load(keyResources);

    File androidKeystore;
    String androidKeystorePassword;
    String androidCertificatePassword;
    String androidCertificateAlias;

    if (androidServer != null) {
      AuthenticationInfo info = wagonManager.getAuthenticationInfo(androidServer);
      if (info == null) {
        throw new RuntimeException("Server not found in settings.xml " + androidServer + ".");
      }

      if (info.getPrivateKey() == null) {
        throw new RuntimeException("No private key (keystore) found for server " + androidServer + ".");
      }
      androidKeystore = new File(info.getPrivateKey());

      if (info.getPassphrase() == null) {
        throw new RuntimeException("No passphrase (keystore password) found for server " + androidServer + ".");
      }
      androidKeystorePassword = info.getPassphrase();

      if (info.getUserName() == null) {
        throw new RuntimeException("No username (certificate alias) found for server " + androidServer + ".");
      }
      androidCertificateAlias = info.getUserName();

      if (info.getPassword() == null) {
        throw new RuntimeException("No password (certificate password) found for server " + androidServer + ".");
      }
      androidCertificatePassword = info.getPassword();
    } else {
      getLog().warn("androidServer not specified, falling back to properties instead.");
      androidKeystore = this.androidKeystore;
      androidKeystorePassword = this.androidKeystorePassword;
      androidCertificatePassword = this.androidCertificatePassword;
      androidCertificateAlias = this.androidCertificateAlias;
    }

    if (androidKeystore == null || !androidKeystore.exists()) {
      throw new MojoFailureException("androidKeystore not defined or does not exist.");
    }

    if (androidCertificatePassword == null || androidCertificatePassword.isEmpty()) {
      throw new MojoFailureException("androidCertificatePassword not defined or blank.");
    }

    if (androidKeystorePassword == null || androidKeystorePassword.isEmpty()) {
      throw new MojoFailureException("androidKeystorePassword not defined or blank.");
    }

    if (androidCertificateAlias == null || androidCertificateAlias.isEmpty()) {
      throw new MojoFailureException("androidAlias not defined or blank.");
    }

    if (key != null) {
      getLog().debug("Unlocking existing android key.");
      AndroidKeyUnlockRequest androidKeyUnlockRequest = new AndroidKeyUnlockRequest();
      androidKeyUnlockRequest.setKeyPassword(androidCertificatePassword);
      androidKeyUnlockRequest.setKeyStorePassword(androidKeystorePassword);
      keysManager.unlockKey(webResource, key.getResourcePath(), androidKeyUnlockRequest);
      getLog().debug("Key unlocked.");
      return key.getResourceId();
    }

    getLog().debug("Building android key upload request.");
    AndroidKeyRequest keyRequest =
        createKeyUploadRequest(androidKeystorePassword, androidCertificateAlias, androidCertificatePassword);

    getLog().debug("android key not found, uploading.");
    key = keysManager.postNewKey(webResource, keysResource, keyRequest, androidKeystore);

    getLog().info("Storing new android key id " + key.getResourceId());
    keyIdStore.save(key.getResourceId());

    return key.getResourceId();
  }

  private AndroidKeyRequest createKeyUploadRequest(String androidKeystorePassword, String androidAlias,
      String androidCertificatePassword) {
    AndroidKeyRequest request = new AndroidKeyRequest();
    request.setAlias(androidAlias);
    request.setKeyPassword(androidCertificatePassword);
    request.setKeyStorePassword(androidKeystorePassword);
    request.setTitle(appTitle);
    return request;
  }

  @Override
  public void setAndroidKeyId(Integer androidKeyId) {
    this.androidKeyId = androidKeyId;
  }

  @Override
  public void setAndroidServer(String androidServer) {
    this.androidServer = androidServer;
  }

  @Override
  public void setAndroidCertificatePassword(String androidCertificatePassword) {
    this.androidCertificatePassword = androidCertificatePassword;
  }

  @Override
  public void setAndroidKeystore(File androidKeystore) {
    this.androidKeystore = androidKeystore;
  }

  @Override
  public void setAndroidKeystorePassword(String androidKeystorePassword) {
    this.androidKeystorePassword = androidKeystorePassword;
  }

  @Override
  public void setAndroidCertificateAlias(String androidAlias) {
    this.androidCertificateAlias = androidAlias;
  }

  public Log getLog() {
    return log;
  }

  @Override
  public void setLog(Log log) {
    this.log = log;
  }

  @Override
  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  @Override
  public void setAppTitle(String appTitle) {
    this.appTitle = appTitle;
  }

  public void setKeysManager(KeysManager keysManager) {
    this.keysManager = keysManager;
  }

  public void setWagonManager(WagonManager wagonManager) {
    this.wagonManager = wagonManager;
  }

  public void setKeyIdStore(ResourceIdStore<Key> keyIdStore) {
    this.keyIdStore = keyIdStore;
  }

}
