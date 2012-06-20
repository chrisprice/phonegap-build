package com.github.chrisprice.phonegapbuild.plugin;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.proxy.ProxyInfo;

import com.github.chrisprice.phonegapbuild.api.ApiException;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
import com.github.chrisprice.phonegapbuild.api.managers.KeysManager;
import com.github.chrisprice.phonegapbuild.api.managers.MeManager;
import com.sun.jersey.api.client.WebResource;

/**
 * Handles authentication with the API.
 * 
 * @author cprice
 * 
 */
public abstract class AbstractPhoneGapBuildMojo extends AbstractMojo {

  /**
   * @component role="org.apache.maven.artifact.manager.WagonManager"
   * @required
   * @readonly
   */
  protected WagonManager wagonManager;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.api.managers.AppsManager"
   * @required
   * @readonly
   */
  protected AppsManager appsManager;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.api.managers.KeysManager"
   * @required
   * @readonly
   */
  protected KeysManager keysManager;

  /**
   * @component role="com.github.chrisprice.phonegapbuild.api.managers.MeManager"
   * @required
   * @readonly
   */
  protected MeManager meManager;

  /**
   * The id of the server to pull the credentials from (takes precedent over username/password).
   * 
   * @parameter expression="${phonegap-build.server}"
   */
  private String server;

  /**
   * PhoneGap Build username. Deprecated, use server instead.
   * 
   * @deprecated
   * @parameter expression="${phonegap-build.username}"
   */
  private String username;

  /**
   * PhoneGap Build password. Deprecated, use server instead.
   * 
   * @deprecated
   * @parameter expression="${phonegap-build.password}"
   */
  private String password;

  private WebResource rootWebResource;

  protected WebResource getRootWebResource() {
    if (rootWebResource == null) {
      String username, password;
      if (server != null) {
        AuthenticationInfo info = wagonManager.getAuthenticationInfo(server);
        if (info == null) {
          throw new RuntimeException("Server not found in settings.xml " + server + ".");
        }
        username = info.getUserName();
        if (username == null) {
          throw new RuntimeException("No username found for server " + server + ".");
        }
        password = info.getPassword();
        if (password == null) {
          throw new RuntimeException("No password found for server " + server + ".");
        }
      } else {
        getLog().warn("Server not specified, falling back to username/password.");
        if (this.username == null || this.password == null) {
          throw new RuntimeException("Username/password not specified (" + this.username + ", "
              + this.password + ").");
        }
        username = this.username;
        password = this.password;
      }
      ProxyInfo proxyInfo = wagonManager.getProxy("http");
      if (proxyInfo != null) {
        try {
          URI uri = new URI("http", null, proxyInfo.getHost(), proxyInfo.getPort(), null, null, null);
          rootWebResource = meManager.createRootWebResource(username, password, uri.toString());
        } catch (URISyntaxException e) {
          throw new ApiException("Could not load http proxy settings", e);
        }
      } else {
        rootWebResource = meManager.createRootWebResource(username, password);
      }
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

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
