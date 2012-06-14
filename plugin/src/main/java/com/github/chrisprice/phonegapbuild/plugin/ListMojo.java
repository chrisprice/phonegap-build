package com.github.chrisprice.phonegapbuild.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.github.chrisprice.phonegapbuild.api.data.me.MeAppResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Lists any cloud apps or keys.
 * 
 * @goal list
 * @requiresProject false
 */
public class ListMojo extends AbstractPhoneGapBuildMojo {

  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().debug("Authenticating.");
    WebResource webResource = getRootWebResource();

    getLog().info("Requesting summary from cloud.");
    MeResponse me = meManager.requestMe(webResource);

    MeAppResponse[] apps = me.getApps().getAll();

    getLog().info("Found " + apps.length + " apps -");
    for (MeAppResponse app : apps) {
      getLog().info(app.getResourceId() + ":\t" + app.getTitle());
    }

    MeKeyResponse[] iosKeys = me.getKeys().getIos().getAll();
    getLog().info("Found " + iosKeys.length + " ios keys -");
    listKeys(webResource, iosKeys);

    MeKeyResponse[] androidKeys = me.getKeys().getAndroid().getAll();
    getLog().info("Found " + androidKeys.length + " android keys -");
    listKeys(webResource, androidKeys);

  }

  private void listKeys(WebResource webResource, MeKeyResponse[] keys) {
    for (MeKeyResponse key : keys) {
      getLog().info(key.getResourceId() + ":\t" + key.getTitle());
    }
  }
}
