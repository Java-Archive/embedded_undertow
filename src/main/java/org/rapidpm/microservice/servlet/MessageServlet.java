package org.rapidpm.microservice.servlet;

import org.rapidpm.microservice.cdi.Service;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by sven on 27.05.15.
 */
public class MessageServlet extends HttpServlet {

  public MessageServlet() {
    System.out.println("MessageServlet created = ");
  }

  @Inject Service service;

  public static final String MESSAGE = "message";

  private String message;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    message = config.getInitParameter(MESSAGE);
  }

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    writer.write(message + " "+  service.doWork());
    writer.close();
  }

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    doGet(req, resp);
  }
}