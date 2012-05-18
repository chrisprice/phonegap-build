package com.github.chrisprice.phonegapbuild.api.data.apps;

import org.codehaus.jackson.annotate.JsonProperty;

public class AppDetailsRequest {
  public static class Keys {
    private int ios;

    public int getIos() {
      return ios;
    }

    public void setIos(int ios) {
      this.ios = ios;
    }

  }

  private String title;
  @JsonProperty("package")
  private String packageName;
  private String version;
  @JsonProperty("create_method")
  private String createMethod;
  private Keys keys;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCreateMethod() {
    return createMethod;
  }

  public void setCreateMethod(String createMethod) {
    this.createMethod = createMethod;
  }

  public String getPackage() {
    return packageName;
  }

  public void setPackage(String packageName) {
    this.packageName = packageName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Keys getKeys() {
    return keys;
  }

  public void setKeys(Keys keys) {
    this.keys = keys;
  }

}
