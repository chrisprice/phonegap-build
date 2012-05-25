package com.github.chrisprice.phonegapbuild.api.data.me;


import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;

public class MeKeyResponse implements HasResourceIdAndPath<Key> {

  @JsonProperty("default")
  private boolean defaultKey;
  private String title;
  @JsonProperty("id")
  private ResourceId<Key> resourceId;
  @JsonProperty("link")
  private ResourcePath<Key> resourcePath;

  @Override
  public ResourceId<Key> getResourceId() {
    return resourceId;
  }

  public void setResourceId(ResourceId<Key> resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public ResourcePath<Key> getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(ResourcePath<Key> resourcePath) {
    this.resourcePath = resourcePath;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isDefaultKey() {
    return defaultKey;
  }

  public void setDefaultKey(boolean defaultKey) {
    this.defaultKey = defaultKey;
  }

}