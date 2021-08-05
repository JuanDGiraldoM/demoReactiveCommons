package com.example.demoReactiveCommons;

import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableMessageListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDirectAsyncGateway
@EnableMessageListeners
@EnableDomainEventBus
@SpringBootApplication
public class DemoReactiveCommonsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoReactiveCommonsApplication.class, args);
	}

}
