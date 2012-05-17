package com.github.cprice.phonegapbuild.api.data.me;
import com.github.cprice.phonegapbuild.api.data.AbstractResource;
import com.github.cprice.phonegapbuild.api.data.ResourcePath.MeResourcePath;


public class MeResponse extends AbstractResource<MeResourcePath> {

  private int id;
  private String username;
  private String email;
  private MeAppsResponse apps;
  private MeKeysResponse keys;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public MeAppsResponse getApps() {
    return apps;
  }

  public void setApps(MeAppsResponse apps) {
    this.apps = apps;
  }

  public MeKeysResponse getKeys() {
    return keys;
  }

  public void setKeys(MeKeysResponse keys) {
    this.keys = keys;
  }

  @Override
  protected MeResourcePath createResourcePath(String link) {
    return new MeResourcePath(link);
  }

}
