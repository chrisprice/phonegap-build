package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import org.apache.maven.project.MavenProject;

import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.sun.jersey.api.client.WebResource;

public interface AppDownloader {

  public void downloadArtifacts(WebResource webResource, ResourcePath<App> appResourcePath,
      Platform... platforms);

  public void setWorkingDirectory(File workingDirectory);

  public abstract void setProject(MavenProject project);

}