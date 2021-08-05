package com.example.demoReactiveCommons;

import org.reactivecommons.api.domain.Command;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class Component {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    @Autowired
    DomainEventBus eventBus;

    @Autowired
    DirectAsyncGateway gateway;

    @GetMapping("/hello")
    public Mono<String> helloWorld() {
        return Mono.just("Hello world");
    }

    @GetMapping("/sendCommand/{target}")
    public Mono<String> sendCommand(@PathVariable("target") String target) {

        LOGGER.info("Sending message...");

        Message message = Message.builder()
                .message("Send Command")
                .name("Demo Reactive Commons")
                .build();

        LOGGER.info("Message '{}' with name '{}'", message.getMessage(), message.getName());

        Command<Message> command = new Command<>("sendCommand", UUID.randomUUID().toString(), message);

        return gateway.sendCommand(command, target)
                .doOnNext(s -> LOGGER.warn("Should not be called!"))
                .doOnSuccess(s -> LOGGER.info("Empty completion response!"))
                .thenReturn("Ok");
    }

    @GetMapping("/sendEvent")
    public Mono<String> sendEvent() {

        LOGGER.info("Sending message...");

        Message message = Message.builder()
                .message("Send Event")
                .name("Demo Reactive Commons")
                .build();

        LOGGER.info("Message '{}' with name '{}'", message.getMessage(), message.getName());

        DomainEvent<Message> event = new DomainEvent<>("sendEvent", UUID.randomUUID().toString(), message);
        return Mono.from(eventBus.emit(event))
                .doOnNext(s -> LOGGER.warn("Should not be called!"))
                .doOnSuccess(s -> LOGGER.info("Empty completion response!"))
                .thenReturn("Ok");
    }
}
