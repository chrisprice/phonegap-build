package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.legacy.ClassImposteriser;

import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyRequest;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.keys.IOsKeyUnlockRequest;
import com.github.chrisprice.phonegapbuild.api.data.me.MeKeyResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.Key;
import com.github.chrisprice.phonegapbuild.api.data.resources.PlatformKeys;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.sun.jersey.api.client.WebResource;

public class IOsKeyManagerImplTest extends MockObjectTestCase {
  {
    setImposteriser(ClassImposteriser.INSTANCE);
  }
  private File workingDirectory;
  private File iOsMobileProvision;
  private File iOsCertificate;
  private String iOsCertificatePassword;
  private String appTitle;
  private ResourceId<Key> iOsKeyResourceId;
  private IOsKeyManagerImpl iOsKeyManager;

  @Override
  protected void setUp() throws Exception {
    iOsKeyManager = new IOsKeyManagerImpl();

    workingDirectory = File.createTempFile("temp", Long.toString(System.nanoTime()));
    assertTrue(workingDirectory.delete());
    assertTrue(workingDirectory.mkdir());
    iOsKeyManager.setWorkingDirectory(workingDirectory);

    iOsMobileProvision = File.createTempFile("ios", "mobileprovision");
    iOsKeyManager.setiOsMobileProvision(iOsMobileProvision);

    iOsCertificate = File.createTempFile("ios", "p12");
    iOsKeyManager.setiOsCertificate(iOsCertificate);

    iOsCertificatePassword = "blah";
    iOsKeyManager.setiOsCertificatePassword(iOsCertificatePassword);

    appTitle = "title";
    iOsKeyManager.setAppTitle(appTitle);

    iOsKeyResourceId = new ResourceId<Key>(123);

    final Log log = mock(Log.class);
    iOsKeyManager.setLog(log);
    checking(new Expectations() {
      {
        ignoring(log);
      }
    });
  }

  public void testEnsureIOsKeyStored_NoKeyStored() throws MojoExecutionException,
      MojoFailureException, IOException {
    final WebResource webResource = mock(WebResource.class);
    @SuppressWarnings("unchecked")
    final ResourcePath<PlatformKeys> platformKeysResourcePath = mock(ResourcePath.class);
    final MeKeyResponse[] keys = new MeKeyResponse[] {};
    @SuppressWarnings("unchecked")
    final ResourceIdStore<Key> resourceIdStore = mock(ResourceIdStore.class);
    final KeysManager keysManager = mock(KeysManager.class);
    final IOsKeyRequest expectedIOsKeyRequest = new IOsKeyRequest();
    {
      expectedIOsKeyRequest.setTitle(appTitle);
      expectedIOsKeyRequest.setPassword(iOsCertificatePassword);
    }
    final IOsKeyResponse iOsKeyResponse = new IOsKeyResponse();
    {
      iOsKeyResponse.setResourceId(iOsKeyResourceId);
    }
    checking(new Expectations() {
      {
        oneOf(resourceIdStore).setIdOverride(null);
        oneOf(resourceIdStore).setAlias("ios-key");
        oneOf(resourceIdStore).setWorkingDirectory(workingDirectory);
        oneOf(resourceIdStore).load(keys);
        will(returnValue(null));
        oneOf(keysManager).postNewKey(webResource, platformKeysResourcePath, expectedIOsKeyRequest,
            iOsCertificate, iOsMobileProvision);
        will(returnValue(iOsKeyResponse));
        oneOf(resourceIdStore).save(iOsKeyResourceId);
      }
    });
    iOsKeyManager.setKeyIdStore(resourceIdStore);
    iOsKeyManager.setKeysManager(keysManager);
    ResourceId<Key> actualIOsKeyResourceId =
        iOsKeyManager.ensureIOsKey(webResource, platformKeysResourcePath, keys);
    assertEquals(actualIOsKeyResourceId, actualIOsKeyResourceId);
  }

  public void testEnsureIOsKeyStored_KeyStored() throws MojoExecutionException,
      MojoFailureException, IOException {
    final WebResource webResource = mock(WebResource.class);
    @SuppressWarnings("unchecked")
    final ResourcePath<PlatformKeys> platformKeysResourcePath = mock(ResourcePath.class);
    @SuppressWarnings("unchecked")
    final ResourcePath<Key> keyResourcePath = mock(ResourcePath.class, "key");
    final MeKeyResponse key = new MeKeyResponse();
    {
      key.setResourceId(iOsKeyResourceId);
      key.setResourcePath(keyResourcePath);
    }
    final MeKeyResponse[] keys = new MeKeyResponse[] {key};
    @SuppressWarnings("unchecked")
    final ResourceIdStore<Key> resourceIdStore = mock(ResourceIdStore.class);
    final KeysManager keysManager = mock(KeysManager.class);
    final IOsKeyUnlockRequest expectedIOsKeyUnlockRequest = new IOsKeyUnlockRequest();
    {
      expectedIOsKeyUnlockRequest.setPassword(iOsCertificatePassword);
    }
    final IOsKeyResponse iOsKeyResponse = new IOsKeyResponse();
    {
      iOsKeyResponse.setResourceId(iOsKeyResourceId);
    }
    checking(new Expectations() {
      {
        oneOf(resourceIdStore).setIdOverride(null);
        oneOf(resourceIdStore).setAlias("ios-key");
        oneOf(resourceIdStore).setWorkingDirectory(workingDirectory);
        oneOf(resourceIdStore).load(keys);
        will(returnValue(key));
        oneOf(keysManager).unlockKey(webResource, keyResourcePath, expectedIOsKeyUnlockRequest);
        will(returnValue(iOsKeyResponse));
      }
    });
    iOsKeyManager.setKeyIdStore(resourceIdStore);
    iOsKeyManager.setKeysManager(keysManager);
    ResourceId<Key> actualIOsKeyResourceId =
        iOsKeyManager.ensureIOsKey(webResource, platformKeysResourcePath, keys);
    assertEquals(actualIOsKeyResourceId, actualIOsKeyResourceId);
  }

}
