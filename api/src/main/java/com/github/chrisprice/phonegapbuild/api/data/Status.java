package com.github.chrisprice.phonegapbuild.api.data;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonCreator;

/**
 * Represents the build status of an app.
 * 
 * @author cprice
 * 
 */
public enum Status {
  /**
   * a platform for which a key is required but has not being provided
   */
  NULL(null),
  /**
   * build in progress/queued
   */
  PENDING("pending"),
  /**
   * build complete, see app.downloads[platform] for link
   */
  COMPLETE("complete"),
  /**
   * build failed, see app.errors[platfom] for more info
   */
  ERROR("error");

  private static final Map<String, Status> LOOKUP = new HashMap<String, Status>();

  static {
    for (Status s : EnumSet.allOf(Status.class))
      LOOKUP.put(s.value, s);
  }

  private final String value;

  private Status(String value) {
    this.value = value;
  }

  @JsonCreator
  public static Status get(String value) {
    return LOOKUP.get(value);
  }

}
