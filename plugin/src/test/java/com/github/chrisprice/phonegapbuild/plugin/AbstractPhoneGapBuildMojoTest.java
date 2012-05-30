package com.github.chrisprice.phonegapbuild.plugin;

import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

import com.github.chrisprice.phonegapbuild.api.managers.MeManager;
import com.sun.jersey.api.client.WebResource;

public class AbstractPhoneGapBuildMojoTest extends MockObjectTestCase {

  private class PhoneGapBuildMojo extends AbstractPhoneGapBuildMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
      throw new RuntimeException();
    }

    @Override
    public WebResource getRootWebResource() {
      return super.getRootWebResource();
    }
  }

  private PhoneGapBuildMojo mojo;
  private WagonManager wagonManager;
  private MeManager meManager;

  @Override
  protected void setUp() throws Exception {
    mojo = new PhoneGapBuildMojo();
    mojo.setWagonManager(wagonManager = mock(WagonManager.class));
    mojo.setMeManager(meManager = mock(MeManager.class));
  }

  public void testGetRootWebResource_noCredentials() {
    try {
      mojo.getRootWebResource();
      fail("should throw");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage(), e.getMessage().startsWith("Username/password not specified"));
    }
  }

  public void testGetRootWebResource_serverIdSpecifiedButNotFound() {
    mojo.setServer("id");
    checking(new Expectations() {
      {
        oneOf(wagonManager).getAuthenticationInfo("id");
      }
    });
    try {
      mojo.getRootWebResource();
      fail("should throw");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage(), e.getMessage().startsWith("Server not found"));
    }
  }

  public void testGetRootWebResource_serverIdSpecifiedServerFoundButNoUsername() {
    mojo.setServer("id");
    checking(new Expectations() {
      {
        oneOf(wagonManager).getAuthenticationInfo("id");
        will(returnValue(new AuthenticationInfo()));
      }
    });
    try {
      mojo.getRootWebResource();
      fail("should throw");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage(), e.getMessage().startsWith("No username found for server"));
    }
  }

  @SuppressWarnings("serial")
  public void testGetRootWebResource_serverIdSpecifiedServerFoundUsernameSpecifiedButNoPassword() {
    mojo.setServer("id");
    checking(new Expectations() {
      {
        oneOf(wagonManager).getAuthenticationInfo("id");
        will(returnValue(new AuthenticationInfo() {
          {
            setUserName("user");
          }
        }));
      }
    });
    try {
      mojo.getRootWebResource();
      fail("should throw");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage(), e.getMessage().startsWith("No password found for server"));
    }
  }

  @SuppressWarnings("serial")
  public void testGetRootWebResource_serverPositive() {
    mojo.setServer("id");
    checking(new Expectations() {
      {
        oneOf(wagonManager).getAuthenticationInfo("id");
        will(returnValue(new AuthenticationInfo() {
          {
            setUserName("user");
            setPassword("pass");
          }
        }));
        oneOf(meManager).createRootWebResource("user", "pass");
      }
    });
    assertEquals(null, mojo.getRootWebResource());
  }

  public void testGetRootWebResource_usernameSpecifiedNoPassword() {
    mojo.setUsername("user");
    try {
      mojo.getRootWebResource();
      fail("should throw");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage(), e.getMessage().startsWith("Username/password not specified"));
    }
  }

  public void testGetRootWebResource_passwordSpecifiedNoUsername() {
    mojo.setPassword("pass");
    try {
      mojo.getRootWebResource();
      fail("should throw");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage(), e.getMessage().startsWith("Username/password not specified"));
    }
  }

  public void testGetRootWebResource_usernamePasswordPositive() {
    mojo.setUsername("user");
    mojo.setPassword("pass");
    checking(new Expectations() {
      {
        oneOf(meManager).createRootWebResource("user", "pass");
      }
    });
    assertEquals(null, mojo.getRootWebResource());
  }

  @SuppressWarnings("serial")
  public void testGetRootWebResource_precedence() {
    mojo.setServer("id");
    mojo.setUsername("userProperty");
    mojo.setPassword("passProperty");
    checking(new Expectations() {
      {
        oneOf(wagonManager).getAuthenticationInfo("id");
        will(returnValue(new AuthenticationInfo() {
          {
            setUserName("userServer");
            setPassword("passServer");
          }
        }));
        oneOf(meManager).createRootWebResource("userServer", "passServer");
      }
    });
    assertEquals(null, mojo.getRootWebResource());
  }
}
