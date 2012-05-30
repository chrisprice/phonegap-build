package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.plugin.utils.ResourceIdStore;
import com.sun.jersey.api.client.WebResource;

/**
 * Delete the cloud app instance if a stored app id can be found.
 * 
 * @goal clean
 * @phase pre-clean
 */
public class CleanMojo extends AbstractPhoneGapBuildMojo {
  /**
   * Working directory.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build"
   * @readonly
   */
  private File workingDirectory;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.plugin.utils.ResourceIdStore"
   */
  private ResourceIdStore<App> appIdStore;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.plugin.utils.ResourceIdStore"
   */
  private ResourceIdStore<Key> keyIdStore;

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().debug("Authenticating.");

    WebResource webResource = getRootWebResource();

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

  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public void setAppIdStore(ResourceIdStore<App> appIdStore) {
    this.appIdStore = appIdStore;
  }

  public void setKeyIdStore(ResourceIdStore<Key> keyIdStore) {
    this.keyIdStore = keyIdStore;
  }

}
