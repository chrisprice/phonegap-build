package com.github.chrisprice.phonegapbuild.api.managers;



import java.io.File;

import javax.ws.rs.core.MediaType;

import com.github.chrisprice.phonegapbuild.api.ApiException;
import com.github.chrisprice.phonegapbuild.api.data.AppFileExtensions;
import com.github.chrisprice.phonegapbuild.api.data.ErrorResponse;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.AppDownloadResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.AppResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath.AppsResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.SuccessResponse;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppPlatformKeysResponse;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppResponse;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppsResponse;
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

  public File downloadApp(WebResource resource, AppResourcePath appResourcePath, Platform platform,
      File targetDirectory) {
    try {
      AppResponse app = null;
      AppDownloadResourcePath path = null;
      while (path == null) {
        // do some waiting logic
        Thread.sleep(5000);
        app = getApp(resource, appResourcePath);
        path = app.getDownload().get(platform);
      }
      File tempFile = resource.path(path.getPath()).get(File.class);
      String extension = AppFileExtensions.get(platform, isSigned(platform, app));
      File targetFile = new File(targetDirectory, app.getTitle() + "." + extension);
      if (!tempFile.renameTo(targetFile)) {
        throw new ApiException("Could not move/rename downloaded file. It may still be available at "
            + tempFile.getAbsolutePath() + ".");
      }
      return targetFile;
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    } catch (InterruptedException e) {
      throw new ApiException("Interupted whilst waiting for download to become available", e);
    }
  }

  protected boolean isSigned(Platform platform, AppResponse app) {
    AppPlatformKeysResponse platformKeysResponse = app.getKeys().get(platform);
    return platformKeysResponse != null && platformKeysResponse.getAll().length > 0;
  }

}
