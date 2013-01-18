package com.github.chrisprice.phonegapbuild.api.data.keys;

import org.codehaus.jackson.annotate.JsonProperty;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;

public class IOsKeyResponse implements HasResourceIdAndPath<Key> {
  @JsonProperty("id")
  private ResourceId<Key> resourceId;
  private String title;
  @JsonProperty("default")
  private boolean defaultKey;
  @JsonProperty("cert_name")
  private String certificateName;
  private String provision;
  private boolean locked;
  private String role;
  @JsonProperty("link")
  private ResourcePath<Key> resourcePath;

  @Override
  public ResourcePath<Key> getResourcePath() {
    return resourcePath;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isDefaultKey() {
    return defaultKey;
  }

  public void setDefaultKey(boolean defaultKey) {
    this.defaultKey = defaultKey;
  }

  public String getCertificateName() {
    return certificateName;
  }

  public void setCertificateName(String certificateName) {
    this.certificateName = certificateName;
  }

  public String getProvision() {
    return provision;
  }

  public void setProvision(String provision) {
    this.provision = provision;
  }

  @Override
  public ResourceId<Key> getResourceId() {
    return resourceId;
  }

  public void setResourceId(ResourceId<Key> resourceId) {
    this.resourceId = resourceId;
  }

  public void setResourcePath(ResourcePath<Key> resourcePath) {
    this.resourcePath = resourcePath;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
