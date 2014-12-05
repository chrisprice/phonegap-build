package com.github.chrisprice.phonegapbuild.api.data.apps;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.HasAllPlatforms;
import com.github.chrisprice.phonegapbuild.api.data.Platform;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppErrorResponse implements HasAllPlatforms<String> {
  private String android;
  private String blackberry;
  private String ios;
  private String symbian;
  private String webos;
  private String winphone;

  public String getAndroid() {
    return android;
  }

  public void setAndroid(String android) {
    this.android = android;
  }

  public String getBlackberry() {
    return blackberry;
  }

  public void setBlackberry(String blackberry) {
    this.blackberry = blackberry;
  }

  public String getIos() {
    return ios;
  }

  public void setIos(String ios) {
    this.ios = ios;
  }

  public String getSymbian() {
    return symbian;
  }

  public void setSymbian(String symbian) {
    this.symbian = symbian;
  }

  public String getWebos() {
    return webos;
  }

  public void setWebos(String webos) {
    this.webos = webos;
  }

  public String getWinphone() {
    return winphone;
  }

  public void setWinphone(String winphone) {
    this.winphone = winphone;
  }

  @Override
  public String get(Platform platform) {
    return platform.get(this);
  }

}
