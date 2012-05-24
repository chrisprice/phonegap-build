package com.github.chrisprice.phonegapbuild.api.data;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Platform {

  ANDROID("android") {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getAndroid();
    }
  },

  BLACKBERRY("blackberry") {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getBlackberry();
    }
  },
  IOS("ios") {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getIos();
    }
  },
  SYMBIAN("symbian") {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getSymbian();
    }
  },
  WEBOS("webos") {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getWebos();
    }
  },
  WINPHONE("winphone") {
    public <T> T get(HasAllPlatforms<T> hasAllPlatforms) {
      return hasAllPlatforms.getWinphone();
    }
  };

  private static final Map<String, Platform> LOOKUP = new HashMap<String, Platform>();

  static {
    for (Platform s : EnumSet.allOf(Platform.class))
      LOOKUP.put(s.value, s);
  }

  private final String value;

  private Platform(String value) {
    this.value = value;
  }

  public abstract <T> T get(HasAllPlatforms<T> hasAllPlatforms);
  
  public static Platform get(String value) {
    return LOOKUP.get(value);
  }

  public String getValue() {
    return value;
  }
}