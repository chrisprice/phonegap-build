package com.github.chrisprice.phonegapbuild.api.data.me;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Apps;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeAppsResponse implements HasResourcePath<Apps> {

  private MeAppResponse[] all;
  @JsonProperty("link")
  private ResourcePath<Apps> resourcePath;

  @Override
  public ResourcePath<Apps> getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(ResourcePath<Apps> resourcePath) {
    this.resourcePath = resourcePath;
  }
  public MeAppResponse[] getAll() {
    return all;
  }

  public void setAll(MeAppResponse[] all) {
    this.all = all;
  }
}
