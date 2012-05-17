package com.github.cprice.phonegapbuild.api.data.me;

import com.github.cprice.phonegapbuild.api.data.AbstractResource;
import com.github.cprice.phonegapbuild.api.data.ResourcePath.KeysResourcePath;

public class MeKeysResponse extends AbstractResource<KeysResourcePath> {
  private MePlatformResponse ios;
  private MePlatformResponse blackberry;
  private MePlatformResponse android;

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

  @Override
  protected KeysResourcePath createResourcePath(String link) {
    return new KeysResourcePath(link);
  }

}