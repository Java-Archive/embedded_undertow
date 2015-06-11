package org.rapidpm.microservice.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionAttachmentHandler;
import io.undertow.server.session.SessionCookieConfig;
import io.undertow.server.session.SessionManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rapidpm.microservice.servlet.MessageServlet;
import org.rapidpm.microservice.servlet.ServletInstanceFactory;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static io.undertow.servlet.Servlets.*;

/**
 * Created by sven on 26.05.15.
 */
public class ServletTest {

  public static final String MYAPP = "microservice";
  private Undertow server;

  @Before
  public void setUp() throws Exception {
    try {

      DeploymentInfo servletBuilder = deployment()
          .setClassLoader(ServletTest.class.getClassLoader())
          .setContextPath(MYAPP)
          .setDeploymentName("MessageServlet.war")
          .setDefaultEncoding("UTF-8")
          .addServlets(//virtualProxy for Servlet - activate CDI
              servlet("MessageServlet", MessageServlet.class, new ServletInstanceFactory<>(MessageServlet.class))
                  .addInitParam(MessageServlet.MESSAGE, "Hello World")
                  .addMapping("/*"),
              servlet("MyServlet", MessageServlet.class, new ServletInstanceFactory<>(MessageServlet.class))
                  .addInitParam(MessageServlet.MESSAGE, "MyServlet")
                  .addMapping("/myservlet"))
          .addListeners(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class));

      DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
      manager.deploy();

      HttpHandler servletHandler = manager.start();

      PathHandler path = Handlers
          .path(Handlers.redirect(MYAPP))
          .addPrefixPath(MYAPP, servletHandler);


      SessionManager sessionManager = new InMemorySessionManager("SESSION_MANAGER");
      SessionCookieConfig sessionConfig = new SessionCookieConfig();
        /*
         * Use the sessionAttachmentHandler to add the sessionManager and
         * sessionCofing to the exchange of every request
         */
      SessionAttachmentHandler sessionAttachmentHandler = new SessionAttachmentHandler(sessionManager, sessionConfig);
      // set as next handler your root handler
      sessionAttachmentHandler.setNext(path);


      server = Undertow.builder()
          .setDirectBuffers(true)
          .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
          .setServerOption(UndertowOptions.ENABLE_SPDY, true)
          .addHttpListener(8080, "localhost")
          .setHandler(path)
          .build();
      server.start();
    } catch (ServletException e) {
      throw new RuntimeException(e);
    }

  }


  @After
  public void tearDown() throws Exception {
    server.stop();
  }


  private final String url = "http://127.0.0.1:8080/" + MYAPP;
  private final String USER_AGENT = "Mozilla/5.0";

  @Test
  public void testServletGetRequest() throws Exception {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    // optional default is GET
    con.setRequestMethod("GET");
    //add request header
    con.setRequestProperty("User-Agent", USER_AGENT);

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    Assert.assertEquals("Hello World CDI Service", response.toString());

  }


  @Test
  public void testServletPostRequest() throws Exception {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    //add reuqest header
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

    // Send post request
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(urlParameters);
    wr.flush();
    wr.close();

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + urlParameters);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    Assert.assertEquals("Hello World CDI Service", response.toString());


  }
}
