package com.github.chrisprice.phonegapbuild.api.managers;

import com.github.chrisprice.phonegapbuild.api.PatchedMultiPartWriter;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class MeManagerImpl implements MeManager {


  private static class TokenResponse {
    private String token;

    public String getToken() {
      return token;
    }
  }

  private static final String TOKEN_PARAM = "auth_token";

  @Override
  public WebResource createRootWebResource(String username, String password) {
    return createRootWebResource(username, password, null);
  }

  @Override
  public WebResource createRootWebResource(String username, String password, String proxyUri) {
	  return createRootWebResource(username, password, proxyUri, null, null);
  }

  @Override
  public WebResource createRootWebResource(String username, String password, String proxyUri, String proxyUser, String proxyPwd ) {
    // create a DefaultApacheHttpClientConfig (instead of just a ClientConfig)
    DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    // configure it to use a proxy
    if (proxyUri != null) {
      config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI, proxyUri);
    }
    // configure it to parse JSON to POJOs
    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    // configure to follow re-directs (used for downloading packages)
    config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_FOLLOW_REDIRECTS, Boolean.TRUE);

    // set proxy auth credentials
  	if ( proxyUser != null && proxyPwd != null ) {
		  config.getState().setProxyCredentials(null, null, -1, proxyUser, proxyPwd );
    }

    // set http auth credentials
    config.getState().setCredentials(null, null, -1, username, password);
    // configure it to explicity use the patched MultiPartWriter
    config.getClasses().add(PatchedMultiPartWriter.class);
    // create an ApacheHttpClient (instead of just a Client)
    ApacheHttpClient c = ApacheHttpClient.create(config);
    // create a resource
    WebResource webResource = c.resource("https://build.phonegap.com");
    // fetch a token
    TokenResponse tokenResponse = requestToken(webResource);
    // associate the token with all future requests
    return webResource.queryParam(TOKEN_PARAM, tokenResponse.getToken());
  }

  private TokenResponse requestToken(WebResource resource) {
    return resource.path(TOKEN_PATH).post(TokenResponse.class);
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.MeManager#requestMe(com.sun.jersey.api.client.WebResource)
   */
  @Override
  public MeResponse requestMe(WebResource resource) {
    return resource.path(API_V1_PATH).get(MeResponse.class);
  }
}
