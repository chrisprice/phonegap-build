package com.github.cprice.phonegapbuild.api.data.me;

import com.github.cprice.phonegapbuild.api.data.AbstractResource;
import com.github.cprice.phonegapbuild.api.data.ResourcePath.AppsResourcePath;

public class MeAppsResponse extends AbstractResource<AppsResourcePath> {

  private int id;
  private MeAppResponse[] all;

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

  @Override
  protected AppsResourcePath createResourcePath(String link) {
    return new AppsResourcePath(link);
  }

}