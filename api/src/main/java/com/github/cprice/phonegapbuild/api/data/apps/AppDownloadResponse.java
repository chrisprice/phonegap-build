package com.github.cprice.phonegapbuild.api.data.apps;

import com.github.cprice.phonegapbuild.api.data.HasAllPlatforms;
import com.github.cprice.phonegapbuild.api.data.Platform;
import com.github.cprice.phonegapbuild.api.data.ResourcePath.AppDownloadResourcePath;

public class AppDownloadResponse implements HasAllPlatforms<AppDownloadResourcePath> {

  private String android;
  private String blackberry;
  private String ios;
  private String symbian;
  private String webos;
  private String winphone;

  public AppDownloadResourcePath getAndroid() {
    return new AppDownloadResourcePath(android);
  }

  public void setAndroid(String android) {
    this.android = android;
  }

  public AppDownloadResourcePath getBlackberry() {
    return new AppDownloadResourcePath(blackberry);
  }

  public void setBlackberry(String blackberry) {
    this.blackberry = blackberry;
  }

  public AppDownloadResourcePath getIos() {
    return new AppDownloadResourcePath(ios);
  }

  public void setIos(String ios) {
    this.ios = ios;
  }

  public AppDownloadResourcePath getSymbian() {
    return new AppDownloadResourcePath(symbian);
  }

  public void setSymbian(String symbian) {
    this.symbian = symbian;
  }

  public AppDownloadResourcePath getWebos() {
    return new AppDownloadResourcePath(webos);
  }

  public void setWebos(String webos) {
    this.webos = webos;
  }

  public AppDownloadResourcePath getWinphone() {
    return new AppDownloadResourcePath(winphone);
  }

  public void setWinphone(String winphone) {
    this.winphone = winphone;
  }

  public AppDownloadResourcePath get(Platform platform) {
    return platform.get(this);
  }
}