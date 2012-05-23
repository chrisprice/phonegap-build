package com.github.chrisprice.phonegapbuild.api.data.me;
import com.github.chrisprice.phonegapbuild.api.data.HasResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Me;


public class MeResponse implements HasResourcePath<Me> {

  private int id;
  private String username;
  private String email;
  private MeAppsResponse apps;
  private MeKeysResponse keys;
  private String link;

  @Override
  public ResourcePath<Me> getResourcePath() {
    return new ResourcePath<Me>(link);
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

}
