package com.github.chrisprice.phonegapbuild.api.data.me;


import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;

public class MeKeyResponse implements HasResourceIdAndPath<Key> {

  @JsonProperty("id")
  private ResourceId<Key> resourceId;
  @JsonProperty("default")
  private boolean defaultKey;
  private String title;
  @JsonProperty("link")
  private ResourcePath<Key> resourcePath;

  @Override
  public ResourcePath<Key> getResourcePath() {
    return resourcePath;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public ResourceId<Key> getResourceId() {
    return resourceId;
  }

  public boolean isDefaultKey() {
    return defaultKey;
  }

  public void setDefaultKey(boolean defaultKey) {
    this.defaultKey = defaultKey;
  }

  public void setResourceId(ResourceId<Key> resourceId) {
    this.resourceId = resourceId;
  }

  public void setResourcePath(ResourcePath<Key> resourcePath) {
    this.resourcePath = resourcePath;
  }

}