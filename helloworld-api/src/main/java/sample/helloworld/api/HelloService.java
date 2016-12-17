/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.api;

import static com.lightbend.lagom.javadsl.api.Service.*;
import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;


import com.lightbend.lagom.javadsl.api.*;
import com.lightbend.lagom.javadsl.api.broker.Topic;



public interface HelloService extends Service {

    ServiceCall<NotUsed, String> hello(String id);

    ServiceCall<String, Source<String, ?>> stream(int interval);

  @Override
  default Descriptor descriptor() {
      return named("helloservice").withCalls(
              pathCall("/api/hello/:id",  this::hello),
              pathCall("/api/stream/:interval",  this::stream))
//      .withAutoAcl(true);

            // here we declare the topic(s) this service will publish to
            .publishing(
                    topic("greetings", this::greetingsTopic)
            )
            .withAutoAcl(true);
  }
  // The topic handle
  Topic<GreetingMessage> greetingsTopic();

}
