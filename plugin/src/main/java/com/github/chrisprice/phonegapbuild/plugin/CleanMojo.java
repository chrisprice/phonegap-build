package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.me.MeAppResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.github.chrisprice.phonegapbuild.api.managers.MeManager;
import com.sun.jersey.api.client.WebResource;

/**
 * Delete the cloud app instance if a stored app id can be found.
 * 
 * @goal clean
 * @phase pre-clean
 */
public class CleanMojo extends AbstractMojo {

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
   * Application identifier file.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/app.id" r
   * @readonly
   */
  private File appIdFile;

  /**
   * iOS signing key identifier
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/ios-key.id"
   * @readonly
   */
  private File iOsKeyIdFile;

  private AppsManager appsManager = new AppsManager();
  private MeManager meManager = new MeManager();
  private KeysManager keysManager = new KeysManager();

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().debug("Authenticating.");

    WebResource webResource = meManager.createRootWebResource(username, password);

    getLog().debug("Requesting summary from cloud.");

    MeResponse me = meManager.requestMe(webResource);

    getLog().debug("Checking for existing app.");

    MeAppResponse appSummary = getStoredAppSummary(me);

    if (appSummary != null) {
      getLog().info("Deleting cloud app id " + appSummary.getId());
      appsManager.deleteApp(webResource, appSummary.getResourcePath());
    }

    getLog().debug("Checking for existing iOS key.");

    HasResourceIdAndPath<Key> iOsKey = getStoredIOsKey(me);

    if (iOsKey != null) {
      getLog().info("Deleting cloud key id " + iOsKey.getResourceId());
      keysManager.deleteKey(webResource, iOsKey.getResourcePath());
    }
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

  public void setAppIdFile(File appIdFile) {
    this.appIdFile = appIdFile;
  }

  public void setAppsManager(AppsManager appsManager) {
    this.appsManager = appsManager;
  }

}
