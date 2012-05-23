package com.github.chrisprice.phonegapbuild.api.data;

import com.github.chrisprice.phonegapbuild.api.data.resources.AbstractResource;

/**
 * Represents a path to a resource, normally obtained from link attributes returned by the API.
 * 
 * @author cprice
 * 
 * @param <T> The type of the resource this link points at.
 */
public class ResourcePath<T extends AbstractResource> {

  private final String path;

  public ResourcePath(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  @Override
  public String toString() {
    return path;
  }

}
