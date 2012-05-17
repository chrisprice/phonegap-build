package com.github.chrisprice.phonegapbuild.api.data.me;

import com.github.chrisprice.phonegapbuild.api.data.AbstractResource;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.PlatformResourcePath;

public class MePlatformResponse extends AbstractResource<PlatformResourcePath> {
  private MeKeyResponse[] all;

  public MeKeyResponse[] getAll() {
    return all;
  }

  public void setAll(MeKeyResponse[] all) {
    this.all = all;
  }

  @Override
  protected PlatformResourcePath createResourcePath(String link) {
    return new PlatformResourcePath(link);
  }

}