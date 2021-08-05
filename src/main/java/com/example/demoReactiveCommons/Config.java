package com.example.demoReactiveCommons;

import com.rabbitmq.client.ConnectionFactory;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.impl.config.ConnectionFactoryProvider;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private void configureSsl(ConnectionFactory connectionFactory) {
        try {
            connectionFactory.useSslProtocol();
        } catch (NoSuchAlgorithmException | KeyManagementException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    @Bean
    public ConnectionFactoryProvider connection(RabbitProperties rabbitProperties) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        configureSsl(connectionFactory);
        return () -> connectionFactory;
    }

    @Bean
    public HandlerRegistry listenerConfig() {
        return HandlerRegistry.register()
                .listenEvent("sendEvent", this::handleEvent, Message.class)
                .handleCommand("sendCommand", this::handleCommand, Message.class);
    }

    public Mono<Void> handleEvent(DomainEvent<Message> domainEvent) {
        LOGGER.info("Event listened: '{}' with name '{}'",
                domainEvent.getData().getMessage(), domainEvent.getData().getName());
        return Mono.empty();
    }

    public Mono<Void> handleCommand(Command<Message> command) {
        LOGGER.info("Command received: '{}' with name '{}'",
                command.getData().getMessage(), command.getData().getName());
        return Mono.empty();
    }
}
