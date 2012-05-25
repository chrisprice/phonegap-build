package com.github.chrisprice.phonegapbuild.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManagerImpl;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManagerImpl;
import com.github.chrisprice.phonegapbuild.api.managers.MeManager;
import com.github.chrisprice.phonegapbuild.api.managers.MeManagerImpl;
import com.sun.jersey.api.client.WebResource;

/**
 * Delete any cloud apps or keys.
 * 
 * @goal scorch
 * @requiresProject false
 */
public class ScorchMojo extends AbstractMojo {

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

  private AppsManager appsManager = new AppsManagerImpl();
  private MeManager meManager = new MeManagerImpl();
  private KeysManager keysManager = new KeysManagerImpl();

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().debug("Authenticating.");
    WebResource webResource = meManager.createRootWebResource(username, password);

    getLog().info("Requesting summary from cloud.");
    MeResponse me = meManager.requestMe(webResource);

    HasResourceIdAndPath<App>[] apps = me.getApps().getAll();

    getLog().info("Found " + apps.length + " apps, starting delete.");
    for (HasResourceIdAndPath<App> app : apps) {
      getLog().info("Deleting cloud app id " + app.getResourceId());
      appsManager.deleteApp(webResource, app.getResourcePath());
    }

    HasResourceIdAndPath<Key>[] keys = me.getKeys().getIos().getAll();

    getLog().info("Found " + keys.length + " keys, starting delete.");
    for (HasResourceIdAndPath<Key> key : keys) {
      getLog().info("Deleting cloud key id " + key.getResourceId());
      keysManager.deleteKey(webResource, key.getResourcePath());
    }

  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setAppsManager(AppsManager appsManager) {
    this.appsManager = appsManager;
  }

  public void setMeManager(MeManager meManager) {
    this.meManager = meManager;
  }

  public void setKeysManager(KeysManager keysManager) {
    this.keysManager = keysManager;
  }

}
