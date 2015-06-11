package org.rapidpm.microservice.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sven on 27.05.15.
 */
@ApplicationPath("/base")
public class JaxRsActivator extends Application {

//hier geht schon @Inject bei TomEE

  @Override
  public Set<Class<?>> getClasses() {
    HashSet<Class<?>> classes = new HashSet<>();
    classes.add(Resource.class);
    classes.add(PojoResource.class);
    return classes;
  }


  /**
   * Hier kann man dann die Proxies holen ?
   * @return
   */
  public Set<Object> getSingletons() {
    return Collections.emptySet();
  }

}