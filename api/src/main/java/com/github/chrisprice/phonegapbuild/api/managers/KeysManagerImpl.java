package com.github.chrisprice.phonegapbuild.api.managers;

import java.io.File;

import javax.ws.rs.core.MediaType;

import com.github.chrisprice.phonegapbuild.api.ApiException;
import com.github.chrisprice.phonegapbuild.api.data.ErrorResponse;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.SuccessResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.AndroidKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.AndroidKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.AndroidKeyUnlockRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyUnlockRequest;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class KeysManagerImpl implements KeysManager {
  @Override
  public SuccessResponse deleteKey(WebResource resource, ResourcePath<Key> keyResourcePath) {
    try {
      return resource.path(keyResourcePath.getPath()).delete(SuccessResponse.class);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  @Override
  public IOsKeyResponse postNewKey(WebResource resource, ResourcePath<PlatformKeys> platformResourcePath,
      IOsKeyRequest iOsKeyRequest, File cert, File profile) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();

      multiPart.bodyPart(new FileDataBodyPart("cert", cert));
      multiPart.bodyPart(new FileDataBodyPart("profile", profile));
      multiPart.bodyPart(new FormDataBodyPart("data", iOsKeyRequest, MediaType.APPLICATION_JSON_TYPE));

      return resource.path(platformResourcePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(
          IOsKeyResponse.class, multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  @Override
  public AndroidKeyResponse postNewKey(WebResource resource, ResourcePath<PlatformKeys> platformResourcePath,
      AndroidKeyRequest androidKeyRequest, File keystore) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();

      multiPart.bodyPart(new FileDataBodyPart("keystore", keystore));
      multiPart.bodyPart(new FormDataBodyPart("data", androidKeyRequest, MediaType.APPLICATION_JSON_TYPE));

      return resource.path(platformResourcePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(
          AndroidKeyResponse.class, multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  @Override
  public IOsKeyResponse unlockKey(WebResource resource, ResourcePath<Key> keyResourcePath,
      IOsKeyUnlockRequest iOsKeyUnlockRequest) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();

      multiPart.bodyPart(new FormDataBodyPart("data", iOsKeyUnlockRequest, MediaType.APPLICATION_JSON_TYPE));

      return resource.path(keyResourcePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).put(
          IOsKeyResponse.class, multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  @Override
  public AndroidKeyResponse unlockKey(WebResource resource, ResourcePath<Key> keyResourcePath,
      AndroidKeyUnlockRequest androidKeyUnlockRequest) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();

      multiPart.bodyPart(new FormDataBodyPart("data", androidKeyUnlockRequest, MediaType.APPLICATION_JSON_TYPE));

      return resource.path(keyResourcePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).put(
          AndroidKeyResponse.class, multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

}
