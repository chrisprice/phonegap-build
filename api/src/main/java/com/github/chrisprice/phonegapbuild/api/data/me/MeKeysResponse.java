package com.github.chrisprice.phonegapbuild.api.data.me;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Keys;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeKeysResponse implements HasResourcePath<Keys> {
  private MePlatformResponse ios;
  private MePlatformResponse blackberry;
  private MePlatformResponse android;
  @JsonProperty("link")
  private ResourcePath<Keys> resourcePath;

  @Override
  public ResourcePath<Keys> getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(ResourcePath<Keys> resourcePath) {
    this.resourcePath = resourcePath;
  }
  public MePlatformResponse getIos() {
    return ios;
  }

  public void setIos(MePlatformResponse ios) {
    this.ios = ios;
  }

  public MePlatformResponse getBlackberry() {
    return blackberry;
  }

  public void setBlackberry(MePlatformResponse blackberry) {
    this.blackberry = blackberry;
  }

  public MePlatformResponse getAndroid() {
    return android;
  }

  public void setAndroid(MePlatformResponse android) {
    this.android = android;
  }

}
