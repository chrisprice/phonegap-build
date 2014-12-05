package com.github.chrisprice.phonegapbuild.api.data.apps;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.HasAllPlatforms;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.AppDownload;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppDownloadResponse implements HasAllPlatforms<ResourcePath<AppDownload>> {

  private String android;
  private String blackberry;
  private String ios;
  private String symbian;
  private String webos;
  private String winphone;

  public ResourcePath<AppDownload> getAndroid() {
    return android == null ? null : new ResourcePath<AppDownload>(android);
  }

  public void setAndroid(String android) {
    this.android = android;
  }

  public ResourcePath<AppDownload> getBlackberry() {
    return blackberry == null ? null : new ResourcePath<AppDownload>(blackberry);
  }

  public void setBlackberry(String blackberry) {
    this.blackberry = blackberry;
  }

  public ResourcePath<AppDownload> getIos() {
    return ios == null ? null : new ResourcePath<AppDownload>(ios);
  }

  public void setIos(String ios) {
    this.ios = ios;
  }

  public ResourcePath<AppDownload> getSymbian() {
    return symbian == null ? null : new ResourcePath<AppDownload>(symbian);
  }

  public void setSymbian(String symbian) {
    this.symbian = symbian;
  }

  public ResourcePath<AppDownload> getWebos() {
    return webos == null ? null : new ResourcePath<AppDownload>(webos);
  }

  public void setWebos(String webos) {
    this.webos = webos;
  }

  public ResourcePath<AppDownload> getWinphone() {
    return winphone == null ? null : new ResourcePath<AppDownload>(winphone);
  }

  public void setWinphone(String winphone) {
    this.winphone = winphone;
  }

  public ResourcePath<AppDownload> get(Platform platform) {
    return platform.get(this);
  }
}
