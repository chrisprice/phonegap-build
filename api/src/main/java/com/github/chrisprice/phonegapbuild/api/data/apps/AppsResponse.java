package com.github.chrisprice.phonegapbuild.api.data.apps;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Apps;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppsResponse implements HasResourcePath<Apps> {
  private AppResponse[] apps;
  private String link;

  @Override
  public ResourcePath<Apps> getResourcePath() {
    return new ResourcePath<Apps>(link);
  }

  public String getLink() {
    return link;
  }
  public AppResponse[] getApps() {
    return apps;
  }

  public void setApps(AppResponse[] apps) {
    this.apps = apps;
  }

}
