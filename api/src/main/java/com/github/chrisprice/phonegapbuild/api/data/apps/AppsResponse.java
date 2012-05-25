package com.github.chrisprice.phonegapbuild.api.data.apps;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Apps;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppsResponse implements HasResourcePath<Apps> {
  private AppResponse[] apps;
  @JsonProperty("link")
  private ResourcePath<Apps> resourcePath;

  @Override
  public ResourcePath<Apps> getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(ResourcePath<Apps> resourcePath) {
    this.resourcePath = resourcePath;
  }
  public AppResponse[] getApps() {
    return apps;
  }

  public void setApps(AppResponse[] apps) {
    this.apps = apps;
  }

}
