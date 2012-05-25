package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.StringUtils;

public class FetchKeysImpl implements FetchKeys {

  private ZipUnArchiver zipUnArchiver;
  private MavenProject project;
  /**
   * The directory to unpack all the archives to.
   */
  private File targetDirectory;
  /**
   * A comma delimited string of the form groupId:artifactId.
   */
  private String includes;


  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeys#execute()
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    targetDirectory.mkdirs();
    zipUnArchiver.setDestDirectory(targetDirectory);
    for (String artifact : includes.split(",")) {
      extractArtifact(artifact);
    }
  }

  private void extractArtifact(String artifact) throws MojoFailureException, MojoExecutionException {
    String[] tokens = StringUtils.split(artifact, ":");
    if (tokens.length != 2) {
      throw new MojoFailureException("Invalid artifact, you must specify groupId:artifactId " + artifact);
    }
    String groupId = tokens[0];
    String artifactId = tokens[1];
    String versionlessKey = ArtifactUtils.versionlessKey(groupId, artifactId);

    Artifact toDownload = (Artifact) project.getArtifactMap().get(versionlessKey);

    try {
      zipUnArchiver.setSourceFile(toDownload.getFile());
      zipUnArchiver.extract();
    } catch (ArchiverException e) {
      throw new MojoExecutionException("Failed to unzip the certificate archive", e);
    }
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeys#setZipUnArchiver(org.codehaus.plexus.archiver.zip.ZipUnArchiver)
   */
  public void setZipUnArchiver(ZipUnArchiver zipUnArchiver) {
    this.zipUnArchiver = zipUnArchiver;
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeys#setProject(org.apache.maven.project.MavenProject)
   */
  public void setProject(MavenProject project) {
    this.project = project;
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeys#setTargetDirectory(java.io.File)
   */
  public void setTargetDirectory(File targetDirectory) {
    this.targetDirectory = targetDirectory;
  }

  /* (non-Javadoc)
   * @see com.github.chrisprice.phonegapbuild.plugin.utils.FetchKeys#setIncludes(java.lang.String)
   */
  public void setIncludes(String includes) {
    this.includes = includes;
  }

}
