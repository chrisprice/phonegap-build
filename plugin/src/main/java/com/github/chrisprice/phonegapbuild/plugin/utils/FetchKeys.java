package com.github.chrisprice.phonegapbuild.plugin.utils;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;

public interface FetchKeys {

  public void execute() throws MojoExecutionException, MojoFailureException;

  public void setZipUnArchiver(ZipUnArchiver zipUnArchiver);

  public void setProject(MavenProject project);

  public void setTargetDirectory(File targetDirectory);

  public void setIncludes(String includes);

}