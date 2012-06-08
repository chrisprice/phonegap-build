package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.sun.jersey.api.client.WebResource;

public interface IOsKeyManager {

  public abstract ResourceId<Key> ensureIOsKey(WebResource webResource,
      ResourcePath<PlatformKeys> keysResource, HasResourceIdAndPath<Key>[] keyResources)
      throws MojoExecutionException, MojoFailureException;

  public abstract void setLog(Log log);

  public abstract void setiOsKeyId(Integer iOsKeyId);

  public abstract void setiOsServer(String iOsServer);

  public abstract void setiOsCertificate(File iOsCertificate);

  public abstract void setiOsCertificatePassword(String iOsCertificatePassword);

  public abstract void setiOsMobileProvision(File iOsMobileProvision);

  public abstract void setKeys(String keys);

  public abstract void setProject(MavenProject project);

  public abstract void setWorkingDirectory(File workingDirectory);

  public abstract void setAppTitle(String appTitle);

}