package com.github.chrisprice.phonegapbuild.api.data;

import com.github.chrisprice.phonegapbuild.api.data.resources.AbstractResource;

/**
 * The id for a resource, normally returned in the id attribute.
 * 
 * @author cprice
 * 
 * @param <T> The type of the resource this id represents.
 */
public class ResourceId<T extends AbstractResource> {
  private final int id;

  public ResourceId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return Integer.toString(id);
  }

}
