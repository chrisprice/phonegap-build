package com.github.chrisprice.phonegapbuild.api.data.me;


import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;

public class MeKeyResponse implements HasResourceIdAndPath<Key> {
  private int id;
  @JsonProperty("default")
  private boolean defaultKey;
  private String title;
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
  public ResourceId<Key> getResourceId() {
    return new ResourceId<Key>(id);
  }

}