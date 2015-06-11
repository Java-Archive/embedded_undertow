package org.rapidpm.microservice.rest;

import org.rapidpm.microservice.cdi.Service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by sven on 27.05.15.
 */
@Path("/test")
public class Resource {

  //wird per Request erzeugt.
  @Inject Service service;

  @GET()
  @Produces("text/plain")
  public String get() {
    return "Hello Rest World " + service.doWork();
  }





}
