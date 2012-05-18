package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.github.chrisprice.phonegapbuild.api.Main;
import com.github.chrisprice.phonegapbuild.api.data.me.MeAppResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
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
   * Application identifier file.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build/app.id" r
   * @readonly
   */
  private File appIdFile;

  private AppsManager appsManager = new AppsManager();

  public void execute() throws MojoExecutionException, MojoFailureException {
    // TODO: disable http client logging

    getLog().debug("Authenticating.");

    WebResource webResource = Main.createRootWebResource();

    getLog().debug("Requesting summary from cloud.");

    MeResponse me = new MeManager().requestMe(webResource);

    getLog().debug("Checking for existing app.");

    MeAppResponse appSummary = getStoredAppSummary(me);

    if (appSummary != null) {
      getLog().info("Deleting cloud app id " + appSummary.getId());
      appsManager.deleteApp(webResource, appSummary.getResourcePath());
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

  public void setAppIdFile(File appIdFile) {
    this.appIdFile = appIdFile;
  }

  public void setAppsManager(AppsManager appsManager) {
    this.appsManager = appsManager;
  }

}
