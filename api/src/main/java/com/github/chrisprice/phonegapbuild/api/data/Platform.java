package com.github.chrisprice.phonegapbuild.api.data;

public enum Platform {

  ANDROID {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getAndroid();
    }
  },

  BLACKBERRY {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getBlackberry();
    }
  },
  IOS {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getIos();
    }
  },
  SYMBIAN {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getSymbian();
    }
  },
  WEBOS {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getWebos();
    }
  },
  WINPHONE {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getWinphone();
    }
  };

  public abstract <T> T get(HasAllPlatforms<T> hasAllPlatforms);
}