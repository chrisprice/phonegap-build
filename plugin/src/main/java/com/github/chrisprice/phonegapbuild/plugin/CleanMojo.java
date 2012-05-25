package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;

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
import com.github.chrisprice.phonegapbuild.plugin.utils.FileResourceIdStore;
import com.github.chrisprice.phonegapbuild.plugin.utils.ResourceIdStore;
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
   * Working directory.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build"
   * @readonly
   */
  private File workingDirectory;

  private AppsManager appsManager = new AppsManagerImpl();
  private MeManager meManager = new MeManagerImpl();
  private KeysManager keysManager = new KeysManagerImpl();
  private ResourceIdStore<App> appIdStore = new FileResourceIdStore<App>();
  private ResourceIdStore<Key> keyIdStore = new FileResourceIdStore<Key>();

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().debug("Authenticating.");

    WebResource webResource = meManager.createRootWebResource(username, password);

    getLog().debug("Requesting summary from cloud.");

    MeResponse me = meManager.requestMe(webResource);

    getLog().debug("Checking for existing app.");

    appIdStore.setAlias("app");
    appIdStore.setWorkingDirectory(workingDirectory);
    HasResourceIdAndPath<App> appSummary = appIdStore.load(me.getApps().getAll());

    if (appSummary != null) {
      getLog().info("Deleting cloud app id " + appSummary.getResourceId());
      appsManager.deleteApp(webResource, appSummary.getResourcePath());
    }

    getLog().debug("Checking for existing iOS key.");

    keyIdStore.setAlias("ios-key");
    keyIdStore.setWorkingDirectory(workingDirectory);
    HasResourceIdAndPath<Key> iOsKey = keyIdStore.load(me.getKeys().getIos().getAll());

    if (iOsKey != null) {
      getLog().info("Deleting cloud key id " + iOsKey.getResourceId());
      keysManager.deleteKey(webResource, iOsKey.getResourcePath());
    }
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
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

  public void setAppIdStore(ResourceIdStore<App> appIdStore) {
    this.appIdStore = appIdStore;
  }

  public void setKeyIdStore(ResourceIdStore<Key> keyIdStore) {
    this.keyIdStore = keyIdStore;
  }

}
