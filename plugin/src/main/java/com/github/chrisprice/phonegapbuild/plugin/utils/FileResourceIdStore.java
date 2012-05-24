package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.resources.AbstractResource;

public class FileResourceIdStore<T extends AbstractResource> implements ResourceIdStore<T> {
  private String alias;

  private File workingDirectory;

  public HasResourceIdAndPath<T> load(HasResourceIdAndPath<T>[] remoteResources) {
    File file = getFile();
    try {
      if (!file.exists()) {
        return null;
      }
      int resourceId = Integer.parseInt(FileUtils.readFileToString(file));
      for (HasResourceIdAndPath<T> resource : remoteResources) {
        if (resource.getResourceId().getId() == resourceId) {
          return resource;
        }
      }
      return null;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read stored resource id", e);
    }
  }

  public void save(ResourceId<T> resourceId) {
    File file = getFile();
    if (resourceId == null) {
      if (file.exists()) {
        file.delete();
      }
      return;
    }
    try {
      FileUtils.writeStringToFile(file, Integer.toString(resourceId.getId()));
    } catch (IOException e) {
      throw new RuntimeException("Failed to store app id", e);
    }
  }

  public void clear() {
    save(null);
  }

  private File getFile() {
    return new File(workingDirectory, alias);
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }
}
