package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
import com.sun.jersey.api.client.WebResource;

public class AppDownloader {
  
  private AppsManager appsManager;
  private MavenProjectHelper mavenProjectHelper;
  private File workingDirectory;
  private MavenProject project;

  public void downloadArtifacts(WebResource webResource, ResourcePath<App> appResourcePath, Platform... platforms) {
    for (Platform platform : platforms) {
      // download the app
      File app = appsManager.downloadApp(webResource, appResourcePath, platform, workingDirectory);
      // attach it to the project with the appropriate classifier (platform) and type (extension)
      mavenProjectHelper.attachArtifact(project, FilenameUtils.getExtension(app.getName()), platform.getValue(), app);
    }
  }

  public void setAppsManager(AppsManager appsManager) {
    this.appsManager = appsManager;
  }

  public void setMavenProjectHelper(MavenProjectHelper mavenProjectHelper) {
    this.mavenProjectHelper = mavenProjectHelper;
  }

  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public void setProject(MavenProject project) {
    this.project = project;
  }

}
