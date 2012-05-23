package com.github.chrisprice.phonegapbuild.api.data;

import com.github.chrisprice.phonegapbuild.api.data.resources.AbstractResource;

public interface HasResourcePath<T extends AbstractResource> {
  public ResourcePath<T> getResourcePath();
}
