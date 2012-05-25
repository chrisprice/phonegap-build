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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ResourceId other = (ResourceId) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
