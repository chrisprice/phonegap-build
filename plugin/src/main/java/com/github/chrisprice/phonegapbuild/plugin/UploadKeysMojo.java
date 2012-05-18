package com.github.chrisprice.phonegapbuild.plugin;

import java.io.File;

public class UploadKeysMojo {

  /**
   * iOS p12 certificate
   * 
   * @parameter expression="${basedir}/src/main/phonegap-build/certificate.p12"
   */
  private File iOsCertificate;

  /**
   * iOS certificate password
   * 
   * @parameter expression="${ios.certificate.password}"
   */
  private String iOsCertificatePassword;

  /**
   * iOS mobileprovision file
   * 
   * @parameter expression="${basedir}/src/main/phonegap-build/team.mobileprovision"
   */
  private File iOsMobileProvision;

  public void setiOsCertificate(File iOsCertificate) {
    this.iOsCertificate = iOsCertificate;
  }

  public void setiOsCertificatePassword(String iOsCertificatePassword) {
    this.iOsCertificatePassword = iOsCertificatePassword;
  }

  public void setiOsMobileProvision(File iOsMobileProvision) {
    this.iOsMobileProvision = iOsMobileProvision;
  }
}
