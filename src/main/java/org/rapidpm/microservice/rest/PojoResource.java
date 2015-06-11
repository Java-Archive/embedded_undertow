package org.rapidpm.microservice.rest;

import com.google.gson.Gson;
import org.rapidpm.microservice.cdi.Service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by svenruppert on 07.06.15.
 */

@Path("pojo")
public class PojoResource {


  //wird per Request erzeugt.
  @Inject Service service;

  @GET()
  @Produces("text/plain")
  public String get() {
//    return  Arrays.asList("A", "B", service.doWork());
    final DataHolder dataHolder = new DataHolder();
    dataHolder.setTxtA("A");
    dataHolder.setTxtb("B");
    return  new Gson().toJson(dataHolder);
  }


}
