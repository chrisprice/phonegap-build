package com.github.chrisprice.phonegapbuild.api.data;

import org.codehaus.jackson.annotate.JsonCreator;

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

  @JsonCreator
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((path == null) ? 0 : path.hashCode());
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
    ResourcePath other = (ResourcePath) obj;
    if (path == null) {
      if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
      return false;
    return true;
  }

}
