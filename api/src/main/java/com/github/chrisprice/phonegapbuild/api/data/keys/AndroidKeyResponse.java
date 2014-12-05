package com.github.chrisprice.phonegapbuild.api.data.keys;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AndroidKeyResponse implements HasResourceIdAndPath<Key> {
  @JsonProperty("id")
  private ResourceId<Key> resourceId;
  private String title;
  @JsonProperty("default")
  private boolean defaultKey;
  private String alias;
  private boolean locked;
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

  public boolean isDefaultKey() {
    return defaultKey;
  }

  public void setDefaultKey(boolean defaultKey) {
    this.defaultKey = defaultKey;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Override
  public ResourceId<Key> getResourceId() {
    return resourceId;
  }

  public void setResourceId(ResourceId<Key> resourceId) {
    this.resourceId = resourceId;
  }

  public void setResourcePath(ResourcePath<Key> resourcePath) {
    this.resourcePath = resourcePath;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }
}
