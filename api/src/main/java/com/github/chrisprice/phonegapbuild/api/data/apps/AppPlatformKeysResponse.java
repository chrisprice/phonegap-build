package com.github.chrisprice.phonegapbuild.api.data.apps;

import com.github.chrisprice.phonegapbuild.api.data.AbstractResource;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;

public class AppPlatformKeysResponse extends AbstractResource<ResourcePath> {

  private AppPlatformKeyResponse[] all;

  @Override
  protected ResourcePath createResourcePath(String link) {
    return null;
  }

  public AppPlatformKeyResponse[] getAll() {
    return all;
  }

  public void setAll(AppPlatformKeyResponse[] all) {
    this.all = all;
  }
}
