package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.sun.jersey.api.client.WebResource;

public interface AndroidKeyManager {

  public ResourceId<Key> ensureAndroidKey(WebResource webResource, ResourcePath<PlatformKeys> keysResource,
      HasResourceIdAndPath<Key>[] keyResources) throws MojoFailureException;

  public void setAndroidKeyId(Integer androidKeyId);

  public void setAndroidServer(String androidServer);

  public void setAndroidCertificatePassword(String androidCertificatePassword);

  public void setAndroidKeystore(File androidKeystore);

  public void setAndroidKeystorePassword(String androidKeystorePassword);

  public void setAndroidCertificateAlias(String androidCertificateAlias);

  public void setLog(Log log);

  public void setWorkingDirectory(File workingDirectory);

  public void setAppTitle(String appTitle);

}