package com.github.chrisprice.phonegapbuild.api.data.me;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Keys;

public class MeKeysResponse implements HasResourcePath<Keys> {
  private MePlatformResponse ios;
  private MePlatformResponse blackberry;
  private MePlatformResponse android;
  private String link;

  @Override
  public ResourcePath<Keys> getResourcePath() {
    return new ResourcePath<Keys>(link);
  }

  public String getLink() {
    return link;
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