import java.io.File;

import javax.ws.rs.core.MediaType;

import post.AppDetailsRequest;

import com.github.cprice.phonegapbuild.api.data.ErrorResponse;
import com.github.cprice.phonegapbuild.api.data.ResourcePath.AppResourcePath;
import com.github.cprice.phonegapbuild.api.data.ResourcePath.AppsResourcePath;
import com.github.cprice.phonegapbuild.api.data.SuccessResponse;
import com.github.cprice.phonegapbuild.api.data.apps.AppResponse;
import com.github.cprice.phonegapbuild.api.data.apps.AppsResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class AppsManager {
  public AppsResponse getApps(WebResource resource, AppsResourcePath appsResponsePath) {
    return resource.path(appsResponsePath.getPath()).get(AppsResponse.class);
  }

  public AppResponse postNewApp(WebResource resource, AppsResourcePath appsResponsePath, AppDetailsRequest appsRequest,
      File file) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();

      multiPart.bodyPart(new FileDataBodyPart("file", file));
      multiPart.bodyPart(new FormDataBodyPart("data", appsRequest, MediaType.APPLICATION_JSON_TYPE));
      return resource.path(appsResponsePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(AppResponse.class,
          multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  public AppResponse getApp(WebResource resource, AppResourcePath appResourcePath) {
    return resource.path(appResourcePath.getPath()).get(AppResponse.class);
  }

  public AppResponse putApp(WebResource resource, AppResourcePath appResourcePath, AppDetailsRequest appsRequest, File file) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();

      multiPart.bodyPart(new FileDataBodyPart("file", file));
      multiPart.bodyPart(new FormDataBodyPart("data", appsRequest, MediaType.APPLICATION_JSON_TYPE));
      return resource.path(appResourcePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).put(AppResponse.class,
          multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  public SuccessResponse deleteApp(WebResource resource, AppResourcePath appResourcePath) {
    try {
      return resource.path(appResourcePath.getPath()).delete(SuccessResponse.class);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  public File downloadAndroidApp(WebResource resource, AppResourcePath appResourcePath, File targetDirectory) {
    try {
      AppResponse app = getApp(resource, appResourcePath);

      while (app.getDownload().getAndroid() == null) {
        // do some waiting logic
        Thread.sleep(5000);
        app = getApp(resource, appResourcePath);
      }
      File file = resource.path(app.getDownload().getAndroid()).get(File.class);
      if (!file.renameTo(new File(targetDirectory, app.getTitle() + ".apk"))) {
        throw new ApiException("Could not move/rename downloaded file. It may still be available at "
            + file.getAbsolutePath() + ".");
      }
      return file;
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    } catch (InterruptedException e) {
      throw new ApiException("Interupted whilst waiting for download to become available", e);
    }
  }

}
