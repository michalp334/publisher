/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.subscriber.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import sample.helloworld.api.GreetingMessage;
import sample.helloworld.api.HelloService;
import sample.subscriber.api.SubscriberService;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.CompletableFuture.completedFuture;


public class SubscriberServiceImpl implements SubscriberService {


  Logger log = LoggerFactory.getLogger(SubscriberServiceImpl.class);

  private final HelloService helloService;
  private final Set<GreetingMessage> messageSet = new HashSet<GreetingMessage>(0);

  @Inject
  public SubscriberServiceImpl(HelloService helloService) {
    this.helloService = helloService;
  }


    @Override
    public ServiceCall<NotUsed, String> read() {
        return request -> {

            log.info("read() called");

            //to się normalnie powinno wykonywać i zwracać Done w próżnię
            SubscriberService subscriberService = helloService.greetingsTopic()
                    .subscribe();// <-- you get back a Subscriber instance

            log.info(subscriberService);

            subscriberService
                    .atLeastOnce(Flow.fromFunction((GreetingMessage message) -> {
                        //tu wiemy że nie dochodzimy - albo
                        log.info("inside .atLeastOnce() thingie");
                        return doSomethingWithTheMessage(message); //side-effects
                    }));
            log.info("exited greetingsTopic().subscribe()... thingie");
            return completedFuture(messageSet.toString());
        };
    }

    private Done doSomethingWithTheMessage(GreetingMessage message) {
      log.info("doSomethingWithTheMessage() called. Message is; " + message);
      messageSet.add(message);
      return Done.getInstance();
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        SubscriberServiceImpl that = (SubscriberServiceImpl) o;
//
//        if (helloService != null ? !helloService.equals(that.helloService) : that.helloService != null) return false;
//        return messageSet.equals(that.messageSet);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = helloService != null ? helloService.hashCode() : 0;
//        result = 31 * result + messageSet.hashCode();
//        return result;
//    }
}
