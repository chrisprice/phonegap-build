import get.AppResponse;
import get.MeResponse;

import java.io.File;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class Main {

  private static final String API_V1_PATH = "/api/v1";

  /**
   * @param args
   */
  public static void main(String[] args) {
    // create a DefaultApacheHttpClientConfig (instead of just a ClientConfig)
    DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    // configure it to use the fiddler proxy
    config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI, "http://127.0.0.1:8888");
    // configure it to parse JSON to POJOs
    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    // set http auth credentials
    config.getState().setCredentials(null, null, -1, "price.c@gmail.com", "crack_this_bitch");
    // configure it to explicity use the patched MultiPartWriter
    config.getClasses().add(PatchedMultiPartWriter.class);
    // create an ApacheHttpClient (instead of just a Client)
    ApacheHttpClient c = ApacheHttpClient.create(config);
    // create a resource
    // WebResource r = c.resource("http://dev.scottlogic.co.uk");
    WebResource r = c.resource("https://build.phonegap.com");

    // set some params and fire it off
    MeResponse me = r.path(API_V1_PATH).get(MeResponse.class);
    // use the response
    System.out.println(me.getApps().getAll().length);
    for (MeResponse.Apps.App app : me.getApps().getAll()) {
      SuccessResponse response = r.path(app.getLink()).delete(SuccessResponse.class);
      System.out.println(response.getSuccess());
    }

    // createThenDeleteApp(r, me);
  }

  private static void createThenDeleteApp(WebResource r, MeResponse me) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();
      post.AppsRequest appsRequest = new post.AppsRequest();
      appsRequest.setCreateMethod("file");
      appsRequest.setTitle("API V1 App");
      appsRequest.setPackage("com.alunny.apiv1");
      appsRequest.setVersion("0.1.0");

      multiPart.bodyPart(new FileDataBodyPart("file", new File("c:\\crap\\index.html")));
      multiPart.bodyPart(new FormDataBodyPart("data", appsRequest, MediaType.APPLICATION_JSON_TYPE));
      AppResponse newAppResponse =
          r.path(me.getApps().getLink()).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(AppResponse.class, multiPart);

      System.out.println(newAppResponse.getLink());

      SuccessResponse success = r.path(newAppResponse.getLink()).delete(SuccessResponse.class);
      System.out.println(success.getSuccess());
    } catch (UniformInterfaceException e) {
      System.err.println(e.getResponse().getEntity(ErrorResponse.class).getError());
    }
  }

}
