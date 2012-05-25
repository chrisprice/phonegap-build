package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;

public class AppUploadPackager {
  private File workingDirectory;
  private ZipArchiver zipArchiver;
  private File warDirectory;
  private String[] warIncludes;
  private String[] warExcludes;
  private File configFile;

  public File createUploadPackage() {
    File file = new File(workingDirectory, "file.zip");
    if (file.exists()) {
      if (!file.delete()) {
        throw new RuntimeException("Could not delete existing upload package at " + file.getAbsolutePath() + ".");
      }
    }

    try {
      zipArchiver.addDirectory(warDirectory, warIncludes, warExcludes);
      zipArchiver.addFile(configFile, "config.xml");
      zipArchiver.setDestFile(file);
      zipArchiver.createArchive();
    } catch (ArchiverException e) {
      throw new RuntimeException("Failed to create zip for upload at " + file.getAbsolutePath() + ".", e);
    } catch (IOException e) {
      throw new RuntimeException("Failed to create zip for upload at " + file.getAbsolutePath() + ".", e);
    }
    return file;
  }

  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public void setZipArchiver(ZipArchiver zipArchiver) {
    this.zipArchiver = zipArchiver;
  }

  public void setWarDirectory(File warDirectory) {
    this.warDirectory = warDirectory;
  }

  public void setWarIncludes(String[] warIncludes) {
    this.warIncludes = warIncludes;
  }

  public void setWarExcludes(String[] warExcludes) {
    this.warExcludes = warExcludes;
  }

  public void setConfigFile(File configFile) {
    this.configFile = configFile;
  }

}
