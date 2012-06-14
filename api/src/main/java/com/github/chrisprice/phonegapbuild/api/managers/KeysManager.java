package com.github.chrisprice.phonegapbuild.api.managers;

import java.io.File;

import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.SuccessResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.AndroidKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.AndroidKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.sun.jersey.api.client.WebResource;

public interface KeysManager {

  public SuccessResponse deleteKey(WebResource resource, ResourcePath<Key> keyResourcePath);

  public IOsKeyResponse postNewKey(WebResource resource, ResourcePath<PlatformKeys> platformResourcePath,
      IOsKeyRequest iOsKeyRequest, File cert, File profile);

  AndroidKeyResponse postNewKey(WebResource resource, ResourcePath<PlatformKeys> platformResourcePath,
      AndroidKeyRequest androidKeyRequest, File keystore);

}