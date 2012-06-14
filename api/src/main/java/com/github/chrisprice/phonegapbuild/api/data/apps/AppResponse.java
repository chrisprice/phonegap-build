package com.github.chrisprice.phonegapbuild.api.data.apps;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppResponse implements HasResourceIdAndPath<App> {
  private String title;
  private String version;
  private AppStatusResponse status;
  private AppDownloadResponse download;
  private AppErrorResponse error;
  private AppKeysResponse keys;
  private String repo;
  @JsonProperty("id")
  private ResourceId<App> resourceId;
  @JsonProperty("link")
  private ResourcePath<App> resourcePath;

  @Override
  public ResourceId<App> getResourceId() {
    return resourceId;
  }

  public void setResourceId(ResourceId<App> resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public ResourcePath<App> getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(ResourcePath<App> resourcePath) {
    this.resourcePath = resourcePath;
  }
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public AppStatusResponse getStatus() {
    return status;
  }

  public void setStatus(AppStatusResponse status) {
    this.status = status;
  }

  public AppDownloadResponse getDownload() {
    return download;
  }

  public void setDownload(AppDownloadResponse download) {
    this.download = download;
  }

  public String getRepo() {
    return repo;
  }

  public void setRepo(String repo) {
    this.repo = repo;
  }

  public AppKeysResponse getKeys() {
    return keys;
  }

  public void setKeys(AppKeysResponse keys) {
    this.keys = keys;
  }

  public AppErrorResponse getError() {
    return error;
  }

  public void setError(AppErrorResponse error) {
    this.error = error;
  }

}