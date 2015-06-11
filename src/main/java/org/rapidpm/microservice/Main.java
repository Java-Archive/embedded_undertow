package org.rapidpm.microservice;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.rapidpm.microservice.rest.JaxRsActivator;
import org.rapidpm.microservice.servlet.MessageServlet;
import org.rapidpm.microservice.servlet.ServletInstanceFactory;

import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.servlet;

/**
 * Created by svenruppert on 02.06.15.
 */
public class Main {


  private static final String MYAPP = "microservice";
  private static UndertowJaxrsServer server;

  public static void main(String[] args) throws ServletException {
    deploy();

  }

  public static void stop(){
    server.stop();
  }

  public static void deploy() throws ServletException {
    DeploymentInfo servletBuilder = deployMessageServlet();
    DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
    manager.deploy();

    HttpHandler servletHandler = manager.start();
    PathHandler pathServlet = Handlers
        .path(Handlers.redirect(MYAPP))
        .addPrefixPath(MYAPP, servletHandler);

    final Undertow.Builder builder = Undertow.builder()
        .setDirectBuffers(true)
        .setIoThreads(20)
        .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
        .setServerOption(UndertowOptions.ENABLE_SPDY, true)
        .addHttpListener(8081, "localhost") //REST ohne handler
        .addHttpListener(8080, "localhost", pathServlet); //f Servlet
//          .setHandler(pathServlet);

    server = new UndertowJaxrsServer().start(builder);

    final ResteasyDeployment deployment = new ResteasyDeployment();
    deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");
    deployment.setApplication(new JaxRsActivator());
//    deployment.setInjectorFactoryClass();
    server.deploy(server.undertowDeployment(deployment)
        .setDeploymentName("Rest")
        .setContextPath("/base")
        .addListeners(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class))
        .setClassLoader(Main.class.getClassLoader()));
  }

  private static DeploymentInfo deployMessageServlet() {
    return deployment()
          .setClassLoader(Main.class.getClassLoader())
          .setContextPath(MYAPP)
          .setDeploymentName("test.war")
          .setDefaultEncoding("UTF-8")
          .addListeners(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class))
          .addServlets(//virtualProxy for Servlet - activate CDI
              servlet("MessageServlet", MessageServlet.class, new ServletInstanceFactory<>(MessageServlet.class))
                  .addInitParam(MessageServlet.MESSAGE, "Hello World")
                  .addMapping("/*")
          );
  }
}
