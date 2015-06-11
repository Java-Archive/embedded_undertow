package org.rapidpm.microservice.undertow.soap;

import javax.jws.WebService;

/**
 * Created by sven on 22.05.15.
 */
@WebService(targetNamespace = "http://rapidpm.org/wsdl")
public interface PeopleWS {
  String doWork_A();
  String doWork_B(String txt);
}
