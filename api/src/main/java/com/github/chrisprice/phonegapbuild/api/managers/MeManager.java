package com.github.chrisprice.phonegapbuild.api.managers;

import com.github.chrisprice.phonegapbuild.api.PatchedMultiPartWriter;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class MeManager {


  private static class TokenResponse {
    private String token;

    public String getToken() {
      return token;
    }
  }

  public static final String API_V1_PATH = "/api/v1";
  public static final String TOKEN_PATH = "/token";
  private static final String TOKEN_PARAM = "auth_token";

  public WebResource createRootWebResource(String username, String password) {
    // create a DefaultApacheHttpClientConfig (instead of just a ClientConfig)
    DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    // configure it to use the fiddler proxy
    config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI, "http://127.0.0.1:8888");
    // configure it to parse JSON to POJOs
    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    // configure to follow re-directs (used for downloading packages)
    config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_FOLLOW_REDIRECTS, Boolean.TRUE);
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

  public MeResponse requestMe(WebResource resource) {
    return resource.path(API_V1_PATH).get(MeResponse.class);
  }
}
