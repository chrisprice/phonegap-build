package com.github.chrisprice.phonegapbuild.api.data.apps;

import com.github.chrisprice.phonegapbuild.api.data.HasAllPlatforms;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.AppDownloadResourcePath;

public class AppDownloadResponse implements HasAllPlatforms<AppDownloadResourcePath> {

  private String android;
  private String blackberry;
  private String ios;
  private String symbian;
  private String webos;
  private String winphone;

  public AppDownloadResourcePath getAndroid() {
    return android == null ? null : new AppDownloadResourcePath(android);
  }

  public void setAndroid(String android) {
    this.android = android;
  }

  public AppDownloadResourcePath getBlackberry() {
    return blackberry == null ? null : new AppDownloadResourcePath(blackberry);
  }

  public void setBlackberry(String blackberry) {
    this.blackberry = blackberry;
  }

  public AppDownloadResourcePath getIos() {
    return ios == null ? null : new AppDownloadResourcePath(ios);
  }

  public void setIos(String ios) {
    this.ios = ios;
  }

  public AppDownloadResourcePath getSymbian() {
    return symbian == null ? null : new AppDownloadResourcePath(symbian);
  }

  public void setSymbian(String symbian) {
    this.symbian = symbian;
  }

  public AppDownloadResourcePath getWebos() {
    return webos == null ? null : new AppDownloadResourcePath(webos);
  }

  public void setWebos(String webos) {
    this.webos = webos;
  }

  public AppDownloadResourcePath getWinphone() {
    return winphone == null ? null : new AppDownloadResourcePath(winphone);
  }

  public void setWinphone(String winphone) {
    this.winphone = winphone;
  }

  public AppDownloadResourcePath get(Platform platform) {
    return platform.get(this);
  }
}