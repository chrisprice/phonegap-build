package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.resources.AbstractResource;

public interface ResourceIdStore<T extends AbstractResource> {

  public HasResourceIdAndPath<T> load(HasResourceIdAndPath<T>[] remoteResources);

  public void save(ResourceId<T> resourceId);

  public void clear();

  public void setAlias(String alias);

  public void setWorkingDirectory(File workingDirectory);

  public void setIdOverride(Integer overrideId);

}