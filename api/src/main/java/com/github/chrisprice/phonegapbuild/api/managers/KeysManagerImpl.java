package com.github.chrisprice.phonegapbuild.api.managers;

import java.io.File;

import javax.ws.rs.core.MediaType;

import com.github.chrisprice.phonegapbuild.api.ApiException;
import com.github.chrisprice.phonegapbuild.api.data.ErrorResponse;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.SuccessResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

// TODO: properly support keys for all platforms, this is currently a bit of a hack to make iOS work
public class KeysManagerImpl implements KeysManager {
  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.KeysManager#deleteKey(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath)
   */
  @Override
  public SuccessResponse deleteKey(WebResource resource, ResourcePath<Key> keyResourcePath) {
    try {
      return resource.path(keyResourcePath.getPath()).delete(SuccessResponse.class);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.KeysManager#getKey(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath)
   */
  @Override
  public IOsKeyResponse getKey(WebResource resource, ResourcePath<Key> keyResourcePath) {
    return resource.path(keyResourcePath.getPath()).get(IOsKeyResponse.class);
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.KeysManager#postNewKey(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath, com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest, java.io.File, java.io.File)
   */
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
}
