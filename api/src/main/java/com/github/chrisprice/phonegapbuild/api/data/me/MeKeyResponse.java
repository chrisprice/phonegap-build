package com.github.chrisprice.phonegapbuild.api.data.me;


import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.AbstractResource;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.KeyResourcePath;

public class MeKeyResponse extends AbstractResource<KeyResourcePath> {
  private int id;
  @JsonProperty("default")
  private boolean defaultKey;
  private String title;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isDefault() {
    return defaultKey;
  }

  public void setDefault(boolean defaultKey) {
    this.defaultKey = defaultKey;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  protected KeyResourcePath createResourcePath(String link) {
    return new KeyResourcePath(link);
  }

}