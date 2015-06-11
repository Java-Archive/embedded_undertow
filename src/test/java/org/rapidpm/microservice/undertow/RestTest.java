package org.rapidpm.microservice.undertow;

import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.*;
import org.rapidpm.microservice.Main;
import org.rapidpm.microservice.rest.JaxRsActivator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by sven on 26.05.15.
 */
public class RestTest {
  private static UndertowJaxrsServer server;

  public static final String MYAPP = "microservice";


  @Before
  public void setUp() throws Exception {
    Main.deploy();

  }


  @After
  public void tearDown() throws Exception {
    Main.stop();
  }

  @Test
  public void testApplicationPath() throws Exception {
//    server.deploy(JaxRsActivator.class);

    Client client = ClientBuilder.newClient();
    //MicroRestApp Path = /base
    //Resource Path = /test

    final String restAppPath = "/base";
    final String ressourcePath = "/test";
    String val = client
        .target(TestPortProvider.generateURL(restAppPath + ressourcePath))
        .request()
        .get(String.class);
    Assert.assertEquals("Hello Rest World CDI Service", val);
    client.close();
  }

  @Test @Ignore
  public void testApplicationContext() throws Exception {
    server.deploy(JaxRsActivator.class, "/root"); // hier fehlt dann CDI

    Client client = ClientBuilder.newClient();
    String val = client
        .target(TestPortProvider.generateURL("/root/test"))
        .request()
        .get(String.class);

    Assert.assertEquals("Hello World", val);
    client.close();
  }

  @Test @Ignore
  public void testDeploymentInfo() throws Exception {
    DeploymentInfo di = server.undertowDeployment(JaxRsActivator.class); // hier fehlt dann CDI

    di.setContextPath("/di");
    di.setDeploymentName("DI");

    server.deploy(di);

    Client client = ClientBuilder.newClient();

    String val = client
        .target(TestPortProvider.generateURL("/di/base/test"))
        .request()
        .get(String.class);

    Assert.assertEquals("hello world", val);
    client.close();
  }
}
