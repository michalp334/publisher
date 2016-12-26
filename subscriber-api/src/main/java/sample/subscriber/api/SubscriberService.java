/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.subscriber.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;


public interface SubscriberService extends Service {

    ServiceCall<NotUsed, String> read();


  @Override
  default Descriptor descriptor() {
      return named("subscriberservice").withCalls(
              pathCall("/api/read",  this::read))
            .withAutoAcl(true);
  }


}
