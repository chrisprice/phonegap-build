package com.github.cprice.phonegapbuild.api.data;

public abstract class AbstractResource<T extends ResourcePath> {
  private String link;

  public T getResourcePath() {
    return createResourcePath(link);
  }

  protected abstract T createResourcePath(String link);

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

}
