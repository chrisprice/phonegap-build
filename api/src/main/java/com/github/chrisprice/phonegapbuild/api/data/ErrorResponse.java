package com.github.chrisprice.phonegapbuild.api.data;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
  private String error;

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
