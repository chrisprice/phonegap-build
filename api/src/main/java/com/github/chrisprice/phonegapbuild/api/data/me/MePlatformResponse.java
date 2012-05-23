package com.github.chrisprice.phonegapbuild.api.data.me;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;

public class MePlatformResponse implements HasResourcePath<PlatformKeys> {
  private MeKeyResponse[] all;
  private String link;

  @Override
  public ResourcePath<PlatformKeys> getResourcePath() {
    return new ResourcePath<PlatformKeys>(link);
  }

  public String getLink() {
    return link;
  }
  public MeKeyResponse[] getAll() {
    return all;
  }

  public void setAll(MeKeyResponse[] all) {
    this.all = all;
  }

}