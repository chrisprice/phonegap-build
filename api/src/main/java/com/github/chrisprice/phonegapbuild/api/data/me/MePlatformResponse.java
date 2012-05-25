package com.github.chrisprice.phonegapbuild.api.data.me;

import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;

public class MePlatformResponse implements HasResourcePath<PlatformKeys> {
  private MeKeyResponse[] all;
  @JsonProperty("link")
  private ResourcePath<PlatformKeys> resourcePath;

  @Override
  public ResourcePath<PlatformKeys> getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(ResourcePath<PlatformKeys> resourcePath) {
    this.resourcePath = resourcePath;
  }
  public MeKeyResponse[] getAll() {
    return all;
  }

  public void setAll(MeKeyResponse[] all) {
    this.all = all;
  }

}