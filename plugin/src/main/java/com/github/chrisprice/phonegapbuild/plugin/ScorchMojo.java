package com.github.chrisprice.phonegapbuild.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.sun.jersey.api.client.WebResource;

/**
 * Delete any cloud apps or keys.
 * 
 * @goal scorch
 * @requiresProject false
 */
public class ScorchMojo extends AbstractPhoneGapBuildMojo {

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().debug("Authenticating.");
    WebResource webResource = getRootWebResource();

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
}
