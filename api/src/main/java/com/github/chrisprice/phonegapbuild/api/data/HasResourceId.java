package com.github.chrisprice.phonegapbuild.api.data;

import com.github.chrisprice.phonegapbuild.api.data.resources.AbstractResource;

public interface HasResourceId<T extends AbstractResource> {
  public ResourceId<T> getResourceId();
}
