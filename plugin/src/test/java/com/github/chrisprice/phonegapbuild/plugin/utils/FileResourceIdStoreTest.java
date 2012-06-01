package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.maven.plugin.MojoExecutionException;

import com.github.chrisprice.phonegapbuild.api.data.HasResourceIdAndPath;
import com.github.chrisprice.phonegapbuild.api.data.ResourceId;
import com.github.chrisprice.phonegapbuild.api.data.me.MeAppResponse;
import com.github.chrisprice.phonegapbuild.api.data.resources.App;

public class FileResourceIdStoreTest extends TestCase {

  private static final String ALIAS = "alias";
  private FileResourceIdStore<App> fileResourceIdStore;
  private File workingDirectory;

  @Override
  protected void setUp() throws Exception {
    fileResourceIdStore = new FileResourceIdStore<App>();
    workingDirectory = File.createTempFile("temp", Long.toString(System.nanoTime()));
    assertTrue(workingDirectory.delete());
    assertTrue(workingDirectory.mkdir());
    fileResourceIdStore.setWorkingDirectory(workingDirectory);
    fileResourceIdStore.setAlias(ALIAS);
  }

  public void testAppAlreadyExistsNoFile() throws IOException, MojoExecutionException {
    assertNull(fileResourceIdStore.load(getEmptyAppList()));
  }

  private MeAppResponse[] getEmptyAppList() {
    return new MeAppResponse[0];
  }

  public void testAppAlreadyExistsNoAssociatedId() throws IOException, MojoExecutionException {
    createFileContainingId(10);
    assertNull(fileResourceIdStore.load(getEmptyAppList()));
  }

  public void testOverrideIdNoAssociatedId() throws IOException, MojoExecutionException {
    try {
      fileResourceIdStore.setIdOverride(10);
      assertNull(fileResourceIdStore.load(getEmptyAppList()));
      fail("Should have thrown");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().startsWith("Override id 10 specified but not found"));
    }
  }

  public void testOverrideIdPositive() throws IOException, MojoExecutionException {
    final int id = 10;
    fileResourceIdStore.setIdOverride(id);
    HasResourceIdAndPath<App> resourceIdAndPath = fileResourceIdStore.load(getAppListContainingId(id));
    assertEquals(id, resourceIdAndPath.getResourceId().getId());
  }

  public void testAppAlreadyExistsPositive() throws IOException, MojoExecutionException {
    final int id = 10;
    createFileContainingId(id);
    MeAppResponse[] resources = getAppListContainingId(id);
    HasResourceIdAndPath<App> resourceIdAndPath = fileResourceIdStore.load(resources);
    assertEquals(id, resourceIdAndPath.getResourceId().getId());
  }

  private MeAppResponse[] getAppListContainingId(int id) {
    MeAppResponse[] meAppResponses = new MeAppResponse[1];
    {
      MeAppResponse meAppResponse = new MeAppResponse();
      meAppResponse.setResourceId(new ResourceId<App>(id));
      meAppResponses[0] = meAppResponse;
    }
    return meAppResponses;
  }

  private File createFileContainingId(int id) throws IOException {
    File file = new File(workingDirectory, ALIAS);
    FileWriter fileWriter = new FileWriter(file);
    fileWriter.append(Integer.toString(id));
    fileWriter.close();
    return file;
  }
}
