package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.component.annotations.Component;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.resources.AbstractResource;

@Component(role = ResourceIdStore.class, hint = "file")
public class FileResourceIdStore<T extends AbstractResource> implements ResourceIdStore<T> {
  private String alias;
  private File workingDirectory;
  private Integer overrideId;

  public HasResourceIdAndPath<T> load(HasResourceIdAndPath<T>[] remoteResources) {
    int resourceId;
    if (overrideId == null) {
      File file = getFile();
      try {
        if (!file.exists()) {
          return null;
        }
        resourceId = Integer.parseInt(FileUtils.readFileToString(file));
      } catch (IOException e) {
        throw new RuntimeException("Failed to read stored resource id for " + alias + ", from file "
            + file.getAbsolutePath(), e);
      }
    } else {
      resourceId = overrideId;
    }
    for (HasResourceIdAndPath<T> resource : remoteResources) {
      if (resource.getResourceId().getId() == resourceId) {
        return resource;
      }
    }
    if (overrideId != null) {
      throw new RuntimeException("Override id " + overrideId + " specified but not found for " + alias);
    }
    return null;
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
      throw new RuntimeException("Failed to store app id for " + alias + ", to file " + file.getAbsolutePath(), e);
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

  @Override
  public void setIdOverride(Integer overrideId) {
    this.overrideId = overrideId;
  }
}
