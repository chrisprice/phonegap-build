package com.github.chrisprice.phonegapbuild.api.data.apps;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppResponse implements HasResourcePath<App> {
  private String title;
  private int id;
  private String version;
  private AppStatusResponse status;
  private AppDownloadResponse download;
  private AppKeysResponse keys;
  private String repo;
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

}