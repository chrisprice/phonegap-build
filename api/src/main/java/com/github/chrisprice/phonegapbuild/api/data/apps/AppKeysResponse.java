package com.github.chrisprice.phonegapbuild.api.data.apps;

import com.github.chrisprice.phonegapbuild.api.data.AbstractResource;
import com.github.chrisprice.phonegapbuild.api.data.HasAllPlatforms;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;

public class AppKeysResponse extends AbstractResource<ResourcePath> implements HasAllPlatforms<AppPlatformKeyResponse> {

  private AppPlatformKeyResponse android;
  private AppPlatformKeyResponse blackberry;
  private AppPlatformKeyResponse ios;
  private AppPlatformKeyResponse symbian;
  private AppPlatformKeyResponse webos;
  private AppPlatformKeyResponse winphone;

  @Override
  protected ResourcePath createResourcePath(String link) {
    return null;
  }

  public AppPlatformKeyResponse getAndroid() {
    return android;
  }

  public void setAndroid(AppPlatformKeyResponse android) {
    this.android = android;
  }

  public AppPlatformKeyResponse getBlackberry() {
    return blackberry;
  }

  public void setBlackberry(AppPlatformKeyResponse blackberry) {
    this.blackberry = blackberry;
  }

  public AppPlatformKeyResponse getIos() {
    return ios;
  }

  public void setIos(AppPlatformKeyResponse ios) {
    this.ios = ios;
  }

  public AppPlatformKeyResponse getSymbian() {
    return symbian;
  }

  public void setSymbian(AppPlatformKeyResponse symbian) {
    this.symbian = symbian;
  }

  public AppPlatformKeyResponse getWebos() {
    return webos;
  }

  public void setWebos(AppPlatformKeyResponse webos) {
    this.webos = webos;
  }

  public AppPlatformKeyResponse getWinphone() {
    return winphone;
  }

  public void setWinphone(AppPlatformKeyResponse winphone) {
    this.winphone = winphone;
  }

  public AppPlatformKeyResponse get(Platform platform) {
    return platform.get(this);
  }
}
