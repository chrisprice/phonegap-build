package com.github.chrisprice.phonegapbuild.plugin;

import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.wagon.authentication.AuthenticationInfo;

import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManagerImpl;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManagerImpl;
import com.github.chrisprice.phonegapbuild.api.managers.MeManager;
import com.github.chrisprice.phonegapbuild.api.managers.MeManagerImpl;
import com.sun.jersey.api.client.WebResource;

/**
 * Handles authentication with the API.
 * 
 * @author cprice
 * 
 */
public abstract class AbstractPhoneGapBuildMojo {

  /**
   * @component role="org.apache.maven.artifact.manager.WagonManager"
   * @required
   * @readonly
   */
  protected WagonManager wagonManager;

  /**
   * @parameter expression="${phonegap-build.server}"
   */
  private String server;

  protected AppsManager appsManager = new AppsManagerImpl();
  protected KeysManager keysManager = new KeysManagerImpl();
  protected MeManager meManager = new MeManagerImpl();

  private WebResource rootWebResource;

  protected WebResource getRootWebResource() {
    if (rootWebResource == null) {
      AuthenticationInfo info = wagonManager.getAuthenticationInfo(server);
      rootWebResource = meManager.createRootWebResource(info.getUserName(), info.getPassword());
    }
    return rootWebResource;
  }

  public void setWagonManager(WagonManager wagonManager) {
    this.wagonManager = wagonManager;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public void setMeManager(MeManager meManager) {
    this.meManager = meManager;
  }

  public void setAppsManager(AppsManager appsManager) {
    this.appsManager = appsManager;
  }

  public void setKeysManager(KeysManager keysManager) {
    this.keysManager = keysManager;
  }

  public void setRootWebResource(WebResource rootWebResource) {
    this.rootWebResource = rootWebResource;
  }

}
