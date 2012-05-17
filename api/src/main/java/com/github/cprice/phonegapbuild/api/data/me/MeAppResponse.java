package com.github.cprice.phonegapbuild.api.data.me;

import com.github.cprice.phonegapbuild.api.data.AbstractResource;
import com.github.cprice.phonegapbuild.api.data.ResourcePath.AppResourcePath;

public class MeAppResponse extends AbstractResource<AppResourcePath> {
  private int id;
  private String title;
  private String role;

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
  protected AppResourcePath createResourcePath(String link) {
    return new AppResourcePath(link);
  }

}