package com.github.chrisprice.phonegapbuild.api.data.keys;

import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.AbstractResource;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.KeyResourcePath;

public class IOsKeyResponse extends AbstractResource<KeyResourcePath> {
  private int id;
  private String title;
  @JsonProperty("default")
  private boolean defaultKey;
  @JsonProperty("cert_name")
  private String certificateName;
  private String provision;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isDefaultKey() {
    return defaultKey;
  }

  public void setDefaultKey(boolean defaultKey) {
    this.defaultKey = defaultKey;
  }

  public String getCertificateName() {
    return certificateName;
  }

  public void setCertificateName(String certificateName) {
    this.certificateName = certificateName;
  }

  public String getProvision() {
    return provision;
  }

  public void setProvision(String provision) {
    this.provision = provision;
  }

  @Override
  protected KeyResourcePath createResourcePath(String link) {
    return new KeyResourcePath(link);
  }
}
