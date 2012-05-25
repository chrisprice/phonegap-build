package com.github.chrisprice.phonegapbuild.api.data.me;

import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Me;


public class MeResponse implements HasResourceIdAndPath<Me> {

  private String username;
  private String email;
  private MeAppsResponse apps;
  private MeKeysResponse keys;
  @JsonProperty("id")
  private ResourceId<Me> resourceId;
  @JsonProperty("link")
  private ResourcePath<Me> resourcePath;

  @Override
  public ResourceId<Me> getResourceId() {
    return resourceId;
  }

  public void setResourceId(ResourceId<Me> resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public ResourcePath<Me> getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(ResourcePath<Me> resourcePath) {
    this.resourcePath = resourcePath;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public MeAppsResponse getApps() {
    return apps;
  }

  public void setApps(MeAppsResponse apps) {
    this.apps = apps;
  }

  public MeKeysResponse getKeys() {
    return keys;
  }

  public void setKeys(MeKeysResponse keys) {
    this.keys = keys;
  }

}
