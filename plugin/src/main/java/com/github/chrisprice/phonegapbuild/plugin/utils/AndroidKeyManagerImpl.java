package com.github.chrisprice.phonegapbuild.plugin.utils;

import org.codehaus.mojo.keytool.KeyTool;
import org.codehaus.mojo.keytool.KeyToolException;
import org.codehaus.mojo.keytool.KeyToolResult;
import org.codehaus.mojo.keytool.requests.KeyToolImportCertificateRequest;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

@Component(role = AndroidKeyManager.class)
public class AndroidKeyManager {

  @Requirement
  private KeyTool keyTool;

  public void doSomething() throws KeyToolException {
    KeyToolImportCertificateRequest request = new KeyToolImportCertificateRequest();
    request.setVerbose(true);
    request.setKeystore("/Users/chris/ChrisPriceKeystore");
    request.setStorepass("changeit");
    request.setAlias("alias");
    request.setKeypass("passphrase");
    request.setFile("/Users/chris/ChrisPrice.p12");

    KeyToolResult result = keyTool.execute(request);
    // control the execution result
    result.getExitCode();
    // get exception
    result.getExecutionException();
  }

  public static void main(String... args) throws KeyToolException, ComponentLookupException {
    PlexusContainer container = new DefaultPlexusContainer();
    AndroidKeyManager keyManager =
        (AndroidKeyManager) container.lookup(AndroidKeyManager.class.getName());
    keyManager.doSomething();
  }
}
