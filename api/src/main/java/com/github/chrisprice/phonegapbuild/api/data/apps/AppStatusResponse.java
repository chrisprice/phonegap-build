package com.github.chrisprice.phonegapbuild.api.data.apps;

import com.github.chrisprice.phonegapbuild.api.data.HasAllPlatforms;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.Status;

public class AppStatusResponse implements HasAllPlatforms<Status> {
  private Status android;
  private Status blackberry;
  private Status ios;
  private Status symbian;
  private Status webos;
  private Status winphone;

  public Status getAndroid() {
    return android;
  }

  public void setAndroid(Status android) {
    this.android = android;
  }

  public Status getBlackberry() {
    return blackberry;
  }

  public void setBlackberry(Status blackberry) {
    this.blackberry = blackberry;
  }

  public Status getIos() {
    return ios;
  }

  public void setIos(Status ios) {
    this.ios = ios;
  }

  public Status getSymbian() {
    return symbian;
  }

  public void setSymbian(Status symbian) {
    this.symbian = symbian;
  }

  public Status getWebos() {
    return webos;
  }

  public void setWebos(Status webos) {
    this.webos = webos;
  }

  public Status getWinphone() {
    return winphone;
  }

  public void setWinphone(Status winphone) {
    this.winphone = winphone;
  }

  @Override
  public Status get(Platform platform) {
    return platform.get(this);
  }

}