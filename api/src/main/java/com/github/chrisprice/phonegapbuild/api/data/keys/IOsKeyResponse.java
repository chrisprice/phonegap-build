package com.github.chrisprice.phonegapbuild.api.data.keys;

import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;

public class IOsKeyResponse implements HasResourceIdAndPath<Key> {
  private int id;
  private String title;
  @JsonProperty("default")
  private boolean defaultKey;
  @JsonProperty("cert_name")
  private String certificateName;
  private String provision;
  private String link;

  @Override
  public ResourcePath<Key> getResourcePath() {
    return new ResourcePath<Key>(link);
  }

  public String getLink() {
    return link;
  }
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
  public ResourceId<Key> getResourceId() {
    return new ResourceId<Key>(id);
  }
}
