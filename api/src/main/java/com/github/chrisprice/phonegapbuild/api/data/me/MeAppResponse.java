package com.github.chrisprice.phonegapbuild.api.data.me;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;

public class MeAppResponse implements HasResourceIdAndPath<App> {
  private int id;
  private String title;
  private String role;
  private String link;

  @Override
  public ResourcePath<App> getResourcePath() {
    return new ResourcePath<App>(link);
  }

  public String getLink() {
    return link;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public ResourceId<App> getResourceId() {
    return new ResourceId<App>(id);
  }

}