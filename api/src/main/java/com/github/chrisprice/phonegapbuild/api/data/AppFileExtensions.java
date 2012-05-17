package com.github.chrisprice.phonegapbuild.api.data;

public final class AppFileExtensions {
  public static String get(Platform platform, boolean signed) {
    switch (platform) {
      case ANDROID:
        return "apk";
      case BLACKBERRY:
        return signed ? "zip" : "jad";
      case IOS:
        return "ipa";
      case SYMBIAN:
        return "wgz";
      case WEBOS:
        return "ipk";
      case WINPHONE:
        return "xap";
    }
    return null;
  }
}
