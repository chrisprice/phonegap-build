package com.github.chrisprice.phonegapbuild.api.managers;

import java.io.File;

import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.ResourcePath;
import com.github.chrisprice.phonegapbuild.api.data.SuccessResponse;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppResponse;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppsResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;
import com.github.chrisprice.phonegapbuild.api.data.resources.Apps;
import com.sun.jersey.api.client.WebResource;

public interface AppsManager {

  AppsResponse getApps(WebResource resource, ResourcePath<Apps> appsResponsePath);

  AppResponse postNewApp(WebResource resource, ResourcePath<Apps> appsResponsePath,
      AppDetailsRequest appsRequest, File file);

  AppResponse getApp(WebResource resource, ResourcePath<App> appResourcePath);

  AppResponse putApp(WebResource resource, ResourcePath<App> appResourcePath, AppDetailsRequest appsRequest,
      File file);

  SuccessResponse deleteApp(WebResource resource, ResourcePath<App> appResourcePath);

  File downloadApp(WebResource resource, ResourcePath<App> appResourcePath, Platform platform,
      File targetDirectory);

  AppResponse updateAppDetails(WebResource resource, ResourcePath<App> keyResourcePath,
	      AppDetailsRequest appDetailsRequest);

}