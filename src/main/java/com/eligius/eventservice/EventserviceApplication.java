package com.eligius.eventservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;

import com.eligius.eventservice.config.Producer;

@SpringBootApplication
public class EventserviceApplication {

    private static final Logger logger = LoggerFactory.getLogger(EventserviceApplication.class);

    private final Producer producer;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EventserviceApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    public EventserviceApplication(Producer producer, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.producer = producer;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            producer.send("anonymous.user.created", "key", "value");

            MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("myConsumer");

            if (listenerContainer != null) {
                listenerContainer.start();
                logger.info("✅ Kafka listener 'myConsumer' started successfully.");
            } else {
                logger.warn("⚠️ Kafka listener 'myConsumer' not found.");
            }
        };
    }
}
