/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.subscriber.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import sample.helloworld.api.HelloService;
import sample.subscriber.api.SubscriberService;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class SubscriberServiceModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(SubscriberService.class, SubscriberServiceImpl.class));
    bindClient(HelloService.class);
  }
}
