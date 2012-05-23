package com.github.chrisprice.phonegapbuild.api.data.me;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Apps;

public class MeAppsResponse implements HasResourcePath<Apps> {

  private int id;
  private MeAppResponse[] all;
  private String link;

  @Override
  public ResourcePath<Apps> getResourcePath() {
    return new ResourcePath<Apps>(link);
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

  public MeAppResponse[] getAll() {
    return all;
  }

  public void setAll(MeAppResponse[] all) {
    this.all = all;
  }
}