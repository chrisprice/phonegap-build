package com.github.chrisprice.phonegapbuild.api.data;

public class ResourcePath {

  public static class MeResourcePath extends ResourcePath {

    public MeResourcePath(String path) {
      super(path);
    }
  }

  public static class AppsResourcePath extends ResourcePath {

    public AppsResourcePath(String path) {
      super(path);
    }
  }
  public static class AppResourcePath extends ResourcePath {

    public AppResourcePath(String path) {
      super(path);
    }
  }
  public static class KeyResourcePath extends ResourcePath {

    public KeyResourcePath(String path) {
      super(path);
    }
  }
  public static class KeysResourcePath extends ResourcePath {

    public KeysResourcePath(String path) {
      super(path);
    }
  }
  public static class PlatformResourcePath extends ResourcePath {

    public PlatformResourcePath(String path) {
      super(path);
    }
  }
  public static class AppDownloadResourcePath extends ResourcePath {

    public AppDownloadResourcePath(String path) {
      super(path);
    }
  }

  private final String path;

  public ResourcePath(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

}
