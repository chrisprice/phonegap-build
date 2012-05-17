package com.github.cprice.phonegapbuild.api.data.apps;

import com.github.cprice.phonegapbuild.api.data.AbstractResource;
import com.github.cprice.phonegapbuild.api.data.HasAllPlatforms;
import com.github.cprice.phonegapbuild.api.data.Platform;
import com.github.cprice.phonegapbuild.api.data.ResourcePath;

public class AppKeysResponse extends AbstractResource<ResourcePath> implements HasAllPlatforms<AppPlatformKeysResponse> {

  private AppPlatformKeysResponse android;
  private AppPlatformKeysResponse blackberry;
  private AppPlatformKeysResponse ios;
  private AppPlatformKeysResponse symbian;
  private AppPlatformKeysResponse webos;
  private AppPlatformKeysResponse winphone;

  @Override
  protected ResourcePath createResourcePath(String link) {
    return null;
  }

  public AppPlatformKeysResponse getAndroid() {
    return android;
  }

  public void setAndroid(AppPlatformKeysResponse android) {
    this.android = android;
  }

  public AppPlatformKeysResponse getBlackberry() {
    return blackberry;
  }

  public void setBlackberry(AppPlatformKeysResponse blackberry) {
    this.blackberry = blackberry;
  }

  public AppPlatformKeysResponse getIos() {
    return ios;
  }

  public void setIos(AppPlatformKeysResponse ios) {
    this.ios = ios;
  }

  public AppPlatformKeysResponse getSymbian() {
    return symbian;
  }

  public void setSymbian(AppPlatformKeysResponse symbian) {
    this.symbian = symbian;
  }

  public AppPlatformKeysResponse getWebos() {
    return webos;
  }

  public void setWebos(AppPlatformKeysResponse webos) {
    this.webos = webos;
  }

  public AppPlatformKeysResponse getWinphone() {
    return winphone;
  }

  public void setWinphone(AppPlatformKeysResponse winphone) {
    this.winphone = winphone;
  }

  public AppPlatformKeysResponse get(Platform platform) {
    return platform.get(this);
  }
}
