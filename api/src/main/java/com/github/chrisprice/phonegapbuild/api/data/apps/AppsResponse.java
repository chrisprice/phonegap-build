package com.github.chrisprice.phonegapbuild.api.data.apps;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.AbstractResource;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.AppsResourcePath;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppsResponse extends AbstractResource<AppsResourcePath> {
  private AppResponse[] apps;

  public AppResponse[] getApps() {
    return apps;
  }

  public void setApps(AppResponse[] apps) {
    this.apps = apps;
  }

  @Override
  protected AppsResourcePath createResourcePath(String link) {
    return new AppsResourcePath(link);
  }

}
