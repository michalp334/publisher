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

import static java.util.concurrent.CompletableFuture.completedFuture;


public class SubscriberServiceImpl implements SubscriberService {

  private final HelloService helloService;
  private final Set<GreetingMessage> messageSet = new HashSet<GreetingMessage>(0);

  @Inject
  public SubscriberServiceImpl(HelloService helloService) {
    this.helloService = helloService;
  }


    @Override
    public ServiceCall<NotUsed, String> read() {
        return request -> {
            helloService.greetingsTopic()
                    .subscribe() // <-- you get back a Subscriber instance
                    .atLeastOnce(Flow.fromFunction((GreetingMessage message) -> {
                        return doSomethingWithTheMessage(message); //side-effects
                    }));
            return completedFuture(messageSet.toString());
        };
    }

    private Done doSomethingWithTheMessage(GreetingMessage message) {
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
