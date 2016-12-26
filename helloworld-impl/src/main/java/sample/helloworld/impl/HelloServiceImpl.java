/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.helloworld.impl;

import akka.NotUsed;
import akka.japi.Pair;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;
import sample.helloworld.api.GreetingMessage;
import sample.helloworld.api.HelloService;
import scala.concurrent.duration.FiniteDuration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

  private final PersistentEntityRegistry persistentEntityRegistry;

  @Inject
  public HelloServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(HelloEntity.class);
  }

  @Override
  public ServiceCall<NotUsed, String> hello(String id) {
    return request -> {
      // Look up the hello world entity for the given ID.
        //działa również dla nowych entities (tak było z createAccount)
      PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloEntity.class, id);
      // Ask the entity the Hello command.
      return ref.ask(new HelloCommand.Hello(id, Optional.empty()));
    };
  }

    @Override
    public ServiceCall<String, NotUsed> changeGreeting(String id, String greeting) {
        return request -> {
            // Look up the hello world entity for the given ID.
            PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloEntity.class, id);
            // Ask the entity the Hello command.
            return ref.ask(new HelloCommand.UseGreetingMessage(greeting))
                    .thenApply(ack -> NotUsed.getInstance());
        };
    }

    @Override
  public ServiceCall<String, Source<String, ?>> stream(int intervalMs) {
    return tickMessage -> {
      FiniteDuration interval = FiniteDuration.create(intervalMs, TimeUnit.MILLISECONDS);
      return completedFuture(Source.tick(interval, interval, tickMessage));
    };
  }


  public Topic<GreetingMessage> greetingsTopic() {
    return TopicProducer.singleStreamWithOffset(offset -> {
      return persistentEntityRegistry
              .eventStream(HelloEventTag.INSTANCE, offset)
              .map(this::convertEvent);
    });
  }


    private Pair<GreetingMessage, Offset> convertEvent(Pair<HelloEvent, Offset> helloEventOffsetPair) {

      GreetingMessage message = new GreetingMessage(helloEventOffsetPair.first().getMessage());

      return new Pair<GreetingMessage, Offset>(message, helloEventOffsetPair.second());
    }


}
