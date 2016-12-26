/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import sample.helloworld.api.HelloService;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class HelloServiceModule extends AbstractModule implements ServiceGuiceSupport {

//       * This tells Guice that whenever it sees a dependency on a HelloService,
//      * it should satisfy the dependency using a HelloServiceImpl.
  @Override
  protected void configure() {
    bindServices(serviceBinding(HelloService.class, HelloServiceImpl.class));
  }
}
