package org.rapidpm.microservice.undertow.soap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;

/**
 * Created by svenruppert on 29.05.15.
 */
public class SoapTest {

  private Endpoint endpoint;
  private String address = "http://localhost:9000/PeopleWebService";

  @Before
  public void setUp() throws Exception {
    System.out.println("Starting Server");
    PeopleWSImpl implementor = new PeopleWSImpl(); //muss threadsafe sein..
    //Activate CDI

    endpoint = Endpoint.create(implementor);
    endpoint.setExecutor(Executors.newFixedThreadPool(10));
    endpoint.publish(address);
  }

  @After
  public void tearDown() throws Exception {
    endpoint.stop();
  }

  @Test
  public void testSoap001() throws Exception {

    URL url = new URL(address + "?wsdl");
    final QName qName = new QName("http://rapidpm.org/wsdl", "PeopleWebService");
    Service service = Service.create(url, qName);
    assertNotNull(service);
    PeopleWS peopleWS = service.getPort(PeopleWS.class);
    assertNotNull(peopleWS);

    String reply = peopleWS.doWork_A();
    System.out.println("Server said: " + reply);
//    Assert.assertEquals("doWork_A1000000Service invoced", peopleWS.doWork_A());

  }
}
