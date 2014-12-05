package com.github.chrisprice.phonegapbuild.api.data.me;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeAppResponse implements HasResourceIdAndPath<App> {
  private String title;
  private String role;
  @JsonProperty("id")
  private ResourceId<App> resourceId;
  @JsonProperty("link")
  private ResourcePath<App> resourcePath;

  @Override
  public ResourceId<App> getResourceId() {
    return resourceId;
  }

  public void setResourceId(ResourceId<App> resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public ResourcePath<App> getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(ResourcePath<App> resourcePath) {
    this.resourcePath = resourcePath;
  }
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
