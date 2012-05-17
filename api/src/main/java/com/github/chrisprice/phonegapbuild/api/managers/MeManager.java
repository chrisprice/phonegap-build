package com.github.chrisprice.phonegapbuild.api.managers;

import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.MeResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.sun.jersey.api.client.WebResource;


public class MeManager {

  public static final MeResourcePath API_V1_PATH = new MeResourcePath("/api/v1");
  
  public MeResponse requestMe(WebResource resource) {
    return resource.path(API_V1_PATH.getPath()).get(MeResponse.class);
  }
}
