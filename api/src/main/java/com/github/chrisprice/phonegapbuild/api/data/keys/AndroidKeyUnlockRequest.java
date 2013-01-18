package com.github.chrisprice.phonegapbuild.api.data.keys;

import org.codehaus.jackson.annotate.JsonProperty;

public class AndroidKeyUnlockRequest {
  @JsonProperty("key_pw")
  private String keyPassword;
  @JsonProperty("keystore_pw")
  private String keyStorePassword;

  public String getKeyPassword() {
    return keyPassword;
  }

  public void setKeyPassword(String keyPassword) {
    this.keyPassword = keyPassword;
  }

  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  public void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }
}
