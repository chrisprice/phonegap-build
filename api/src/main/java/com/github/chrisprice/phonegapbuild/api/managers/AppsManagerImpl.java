package com.github.chrisprice.phonegapbuild.api.managers;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import com.github.chrisprice.phonegapbuild.api.ApiException;
import com.github.chrisprice.phonegapbuild.api.data.AppFileExtensions;
import com.github.chrisprice.phonegapbuild.api.data.ErrorResponse;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.SuccessResponse;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppResponse;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppsResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.data.resources.AppDownload;
import com.github.chrisprice.phonegapbuild.api.data.resources.Apps;
import com.google.common.io.Files;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class AppsManagerImpl implements AppsManager {
  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.AppsManager#getApps(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath)
   */
  @Override
  public AppsResponse getApps(WebResource resource, ResourcePath<Apps> appsResponsePath) {
    return resource.path(appsResponsePath.getPath()).get(AppsResponse.class);
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.AppsManager#postNewApp(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath, com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest, java.io.File)
   */
  @Override
  public AppResponse postNewApp(WebResource resource, ResourcePath<Apps> appsResponsePath,
      AppDetailsRequest appsRequest, File file) {
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

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.AppsManager#getApp(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath)
   */
  @Override
  public AppResponse getApp(WebResource resource, ResourcePath<App> appResourcePath) {
    return resource.path(appResourcePath.getPath()).get(AppResponse.class);
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.AppsManager#putApp(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath, com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest, java.io.File)
   */
  @Override
  public AppResponse putApp(WebResource resource, ResourcePath<App> appResourcePath, AppDetailsRequest appsRequest,
      File file) {
    try {
      FormDataMultiPart multiPart = new FormDataMultiPart();

      multiPart.bodyPart(new FileDataBodyPart("file", file));
      if (appsRequest != null) {
        multiPart.bodyPart(new FormDataBodyPart("data", appsRequest, MediaType.APPLICATION_JSON_TYPE));
      }
      return resource.path(appResourcePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).put(AppResponse.class,
          multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.AppsManager#deleteApp(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath)
   */
  @Override
  public SuccessResponse deleteApp(WebResource resource, ResourcePath<App> appResourcePath) {
    try {
      return resource.path(appResourcePath.getPath()).delete(SuccessResponse.class);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.api.managers.AppsManager#downloadApp(com.sun.jersey.api.client.WebResource, com.github.chrisprice.phonegapbuild.api.data.ResourcePath, com.github.chrisprice.phonegapbuild.api.data.Platform, java.io.File)
   */
  @Override
  public File downloadApp(WebResource resource, ResourcePath<App> appResourcePath, Platform platform,
      File targetDirectory) {
    try {
      AppResponse app = null;
      ResourcePath<AppDownload> path = null;
      while (path == null) {
        // do some waiting logic
        Thread.sleep(5000);
        app = getApp(resource, appResourcePath);
        switch (app.getStatus().get(platform)) {
          case PENDING:
            // still waiting, back round the loop we go
            break;
          case COMPLETE:
            // get the download link
            path = app.getDownload().get(platform);
            if (path == null) {
              throw new ApiException("The server responded with a build complete status but no download link.");
            }
            break;
          case ERROR:
            throw new ApiException("Build error : " + app.getError().get(platform));
          case NULL:
            throw new ApiException("This platform requires a key and no key has been defined.");
          default:
            throw new ApiException("The server responded with an unknown status code.");
        }
      }
      File tempFile = resource.path(path.getPath()).get(File.class);
      String extension = AppFileExtensions.get(platform, isSigned(platform, app));
      File targetFile = new File(targetDirectory, app.getTitle() + "." + extension);
      if (targetFile.exists()) {
        if (!targetFile.delete()) {
          throw new ApiException("Could not delete previously downloaded file " + targetFile.getAbsolutePath() + ".");
        }
      }
      try {
        Files.move(tempFile, targetFile);
      } catch (IOException e) {
        throw new ApiException("Could not move/rename downloaded file to " + targetFile.getAbsolutePath()
            + ". It may still be available at " + tempFile.getAbsolutePath() + ".", e);
      }
      return targetFile;
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    } catch (InterruptedException e) {
      throw new ApiException("Interupted whilst waiting for download to become available", e);
    }
  }

  protected boolean isSigned(Platform platform, AppResponse app) {
    return app.getKeys().get(platform) != null;
  }

  @Override
  public AppResponse updateAppDetails(final WebResource resource, final ResourcePath<App> keyResourcePath,
		  final AppDetailsRequest appDetailsRequest) {
    try {
      final FormDataMultiPart multiPart = new FormDataMultiPart();
      multiPart.bodyPart(new FormDataBodyPart("data", appDetailsRequest, MediaType.APPLICATION_JSON_TYPE));
      return resource.path(keyResourcePath.getPath()).type(MediaType.MULTIPART_FORM_DATA_TYPE).put(
              AppResponse.class, multiPart);
    } catch (UniformInterfaceException e) {
      throw new ApiException(e.getResponse().getEntity(ErrorResponse.class), e);
    }
  }

}
