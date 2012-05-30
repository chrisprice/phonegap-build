package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

public interface AppUploadPackager {

  public File createUploadPackage();

  public void setWorkingDirectory(File workingDirectory);

  public void setWarDirectory(File warDirectory);

  public void setWarIncludes(String[] warIncludes);

  public void setWarExcludes(String[] warExcludes);

  public void setConfigFile(File configFile);

}