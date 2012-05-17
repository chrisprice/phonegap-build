package com.github.chrisprice.phonegapbuild.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class BuildMojo extends AbstractMojo {

  /**
   * Says "Hi" to the user.
   * 
   * @goal sayhi
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().info("Hello, world.");
  }

}
