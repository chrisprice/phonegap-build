package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.maven.plugin.MojoExecutionException;

import com.github.chrisprice.phonegapbuild.api.data.me.MeAppResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeAppsResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;

public class BuildMojoTest extends TestCase {

  private BuildMojo mojo;

  @Override
  protected void setUp() throws Exception {
    mojo = new BuildMojo();
  }

  public void testAppAlreadyExistsNoFile() throws IOException, MojoExecutionException {
    mojo.setAppIdFile(getNonExistantFile());
    MeResponse meResponse = getEmptyAppList();
    assertNull(mojo.getStoredAppSummary(meResponse));
  }

  public void testAppAlreadyExistsNoAssociatedId() throws IOException, MojoExecutionException {
    mojo.setAppIdFile(getFileContaining(10));
    MeResponse meResponse = getEmptyAppList();
    assertNull(mojo.getStoredAppSummary(meResponse));
  }

  public void testAppAlreadyExistsPositive() throws IOException, MojoExecutionException {
    int id = 10;
    mojo.setAppIdFile(getFileContaining(id));
    MeResponse meResponse = getAppListContainingId(id);
    assertEquals(id, mojo.getStoredAppSummary(meResponse).getId());
  }

  private MeResponse getEmptyAppList() {
    MeResponse meResponse = new MeResponse();
    {
      MeAppsResponse meAppsResponse = new MeAppsResponse();
      meAppsResponse.setAll(new MeAppResponse[0]);
      meResponse.setApps(meAppsResponse);
    }
    return meResponse;
  }

  private MeResponse getAppListContainingId(int id) {
    MeResponse meResponse = new MeResponse();
    {
      MeAppsResponse meAppsResponse = new MeAppsResponse();
      {
        MeAppResponse[] meAppResponses = new MeAppResponse[1];
        {
          MeAppResponse meAppResponse = new MeAppResponse();
          meAppResponse.setId(id);
          meAppResponses[0] = meAppResponse;
        }
        meAppsResponse.setAll(meAppResponses);
      }
      meResponse.setApps(meAppsResponse);
    }
    return meResponse;
  }

  private File getNonExistantFile() throws IOException {
    File file = File.createTempFile("temp", "file");
    file.delete();
    assert !file.exists();
    return file;
  }

  private File getFileContaining(int id) throws IOException {
    File file = File.createTempFile("temp", "file");
    FileWriter fileWriter = new FileWriter(file);
    fileWriter.append(Integer.toString(id));
    fileWriter.close();
    return file;
  }
}
