import java.io.File;

import com.github.cprice.phonegapbuild.api.data.me.MeResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class Main {

  /**
   * @param args
   * @throws InterruptedException
   */
  public static void main(String[] args) throws InterruptedException {
    WebResource r = createRootWebResource();

    MeManager meManager = new MeManager();
    AppsManager appsManager = new AppsManager();

    MeResponse meResponse = meManager.requestMe(r);
    // AppDetailsRequest appDetailsRequest = new AppDetailsRequest();
    // appDetailsRequest.setCreateMethod("file");
    // appDetailsRequest.setTitle("test");
    // AppResponse newApp =
    // appsManager.postNewApp(r, meResponse.getApps().getResourcePath(), appDetailsRequest, new File(
    // "c:\\crap\\index.html"));

    appsManager.downloadAndroidApp(r, meResponse.getApps().getAll()[0].getResourcePath(), new File("c:\\crap"));
  }

  private static WebResource createRootWebResource() {
    // create a DefaultApacheHttpClientConfig (instead of just a ClientConfig)
    DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    // configure it to use the fiddler proxy
    config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI, "http://127.0.0.1:8888");
    // configure it to parse JSON to POJOs
    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    // configure to follow re-directs (used for downloading packages)
    config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_FOLLOW_REDIRECTS, Boolean.TRUE);
    // set http auth credentials
    config.getState().setCredentials(null, null, -1, "price.c@gmail.com", "crack_this_bitch");
    // configure it to explicity use the patched MultiPartWriter
    config.getClasses().add(PatchedMultiPartWriter.class);
    // create an ApacheHttpClient (instead of just a Client)
    ApacheHttpClient c = ApacheHttpClient.create(config);
    // create a resource
    return c.resource("https://build.phonegap.com");
  }
}
