package com.github.chrisprice.phonegapbuild.api.managers;

import java.io.File;

import javax.ws.rs.core.MediaType;

import com.github.chrisprice.phonegapbuild.api.ApiException;
import com.github.chrisprice.phonegapbuild.api.data.ErrorResponse;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.KeyResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.KeysResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.SuccessResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

// TODO: properly support keys for all platforms, this is currently a bit of a hack to make iOS work
public class KeysManager {
  public SuccessResponse deleteKey(WebResource resource, KeyResourcePath keyResponsePath) {
    try {
      return resource.path(keyResponsePath.getPath()).delete(SuccessResponse.class);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  public IOsKeyResponse getKey(WebResource resource, KeyResourcePath keyResponsePath) {
    return resource.path(keyResponsePath.getPath()).get(IOsKeyResponse.class);
  }

  public IOsKeyResponse postNewKey(WebResource resource, KeysResourcePath keysResponsePath,
      IOsKeyRequest iOsKeyRequest, File cert, File profile) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();

      multiPart.bodyPart(new FileDataBodyPart("cert", cert));
      multiPart.bodyPart(new FileDataBodyPart("profile", profile));
      multiPart.bodyPart(new FormDataBodyPart("data", iOsKeyRequest, MediaType.APPLICATION_JSON_TYPE));
      return resource.path(keysResponsePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(
          IOsKeyResponse.class, multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }
}
