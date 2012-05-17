package com.github.cprice.phonegapbuild.api.data;


public interface HasAllPlatforms<T> {

  public T getAndroid();

  public T getBlackberry();

  public T getIos();

  public T getSymbian();

  public T getWebos();

  public T getWinphone();

  public T get(Platform platform);

}
