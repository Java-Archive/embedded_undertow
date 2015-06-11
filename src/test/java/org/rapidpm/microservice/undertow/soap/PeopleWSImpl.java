package org.rapidpm.microservice.undertow.soap;


import org.rapidpm.microservice.cdi.Service;

import javax.inject.Inject;
import javax.jws.WebService;
import java.time.LocalDateTime;

/**
 * Created by sven on 22.05.15.
 */
@WebService(
    portName = "PeoplePort",
    serviceName = "PeopleWebService",
    targetNamespace = "http://rapidpm.org/wsdl",
    endpointInterface = "org.rapidpm.microservice.undertow.soap.PeopleWS"
)
public class PeopleWSImpl implements PeopleWS{


  @Inject Service service;

  @Override
  public String doWork_A() {
//    return "doWork_A" + service.doWork();
    return "doWork_A" + service;
  }

  @Override
  public String doWork_B(String txt) {
    return txt + " worked on " + LocalDateTime.now().toString();
  }
}
