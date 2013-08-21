package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component(role = AppUploadPackager.class)
public class AppUploadPackagerImpl implements AppUploadPackager {

  @Requirement(hint = "zip")
  private Archiver zipArchiver;

  private File workingDirectory;
  private File warDirectory;
  private String[] warIncludes;
  private String[] warExcludes;
  private File configFile;
  private String zipFile;

  @Override
  public File createUploadPackage() {
    File file = new File(workingDirectory, zipFile);
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

  @Override
  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public void setZipArchiver(ZipArchiver zipArchiver) {
    this.zipArchiver = zipArchiver;
  }

  @Override
  public void setWarDirectory(File warDirectory) {
    this.warDirectory = warDirectory;
  }

  @Override
  public void setWarIncludes(String[] warIncludes) {
    this.warIncludes = warIncludes;
  }

  @Override
  public void setWarExcludes(String[] warExcludes) {
    this.warExcludes = warExcludes;
  }

  @Override
  public void setConfigFile(File configFile) {
    this.configFile = configFile;
  }

@Override
public void setZipFile(String zipFile) {
    this.zipFile = zipFile;
}

}
