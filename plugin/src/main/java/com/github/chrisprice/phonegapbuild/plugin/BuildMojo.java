package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.zip.ZipArchiver;

import com.github.chrisprice.phonegapbuild.api.Main;
import com.github.chrisprice.phonegapbuild.api.data.Platform;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppDetailsRequest;
import com.github.chrisprice.phonegapbuild.api.data.apps.AppResponse;
import com.github.chrisprice.phonegapbuild.api.data.me.MeResponse;
import com.github.chrisprice.phonegapbuild.api.managers.AppsManager;
import com.github.chrisprice.phonegapbuild.api.managers.MeManager;
import com.sun.jersey.api.client.WebResource;

/**
 * Compress the exploded package to a zip using the specified filters. Add in the config.xml from
 * src/main/phonegap-build/.
 * 
 * @goal build
 * @phase package
 */
public class BuildMojo extends AbstractMojo {

  /**
   * The Zip archiver.
   * 
   * @component role="org.codehaus.plexus.archiver.Archiver" roleHint="zip"
   */
  private ZipArchiver zipArchiver;

  /**
   * @component
   */
  private MavenProjectHelper mavenProjectHelper;

  /**
   * @parameter default-value="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * Configuration file.
   * 
   * @parameter expression="${basedir}/src/main/phonegap-build/config.xml"
   */
  private File configFile;

  /**
   * Working directory.
   * 
   * @parameter expression="${project.build.directory}/phonegap-build"
   */
  private File workingDirectory;

  /**
   * The application title. Can also be overridden in the config file.
   * 
   * @parameter expression="${project.build.finalName}"
   */
  private String appTitle;

  /**
   * Exploded WAR directory
   * 
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   */
  private File warDirectory;

  /**
   * A set of file patterns to include in the zip.
   * 
   * @parameter
   */
  private String[] includes;

  /**
   * A set of file patterns to exclude from the zip.
   * 
   * Defaults to "WEB-INF&#47;**&#47;*", "WEB-INF"
   * 
   * @parameter
   */
  private String[] excludes = new String[] {"WEB-INF/**/*", "WEB-INF"};

  public void setExcludes(String[] excludes) {
    this.excludes = excludes;
  }

  public void setIncludes(String[] includes) {
    this.includes = includes;
  }

  public void execute() throws MojoExecutionException, MojoFailureException {

    getLog().info("Creating zip for upload to cloud.");

    workingDirectory.mkdirs();

    final File appSource = new File(workingDirectory, "file.zip");

    try {
      zipArchiver.addDirectory(warDirectory, includes, excludes);
      zipArchiver.addFile(configFile, "config.xml");
      zipArchiver.setDestFile(appSource);
      zipArchiver.createArchive();
    } catch (Exception e) {
      throw new MojoExecutionException("Could not zip", e);
    }

    // disable jersey logging
    java.util.logging.Logger jersey = java.util.logging.Logger.getLogger("com.sun.jersey");
    jersey.setLevel(java.util.logging.Level.OFF);

    getLog().info("Building upload request.");

    AppDetailsRequest appDetailsRequest = new AppDetailsRequest();
    appDetailsRequest.setCreateMethod("file");
    appDetailsRequest.setTitle(appTitle);

    getLog().info("Authenticating.");

    WebResource webResource = Main.createRootWebResource();

    MeManager meManager = new MeManager();
    MeResponse me = meManager.requestMe(webResource);

    getLog().info("Starting upload.");

    AppsManager appsManager = new AppsManager();

    AppResponse newApp =
        appsManager.postNewApp(webResource, me.getApps().getResourcePath(), appDetailsRequest, appSource);

    getLog().info("Starting downloads.");

    File androidApp =
        appsManager.downloadApp(webResource, newApp.getResourcePath(), Platform.ANDROID, workingDirectory);

    mavenProjectHelper.attachArtifact(project, "apk", "android", androidApp);
  }

}
