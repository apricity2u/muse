package com.example.muse.global.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    private static final String EXCHANGE_NAME = "events";
    private static final String NOTIFICATIONS_QUEUE = "notifications_queue";
    private static final String ROUTING_KEY_LIKE = "review.like";

    @Bean
    public TopicExchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATIONS_QUEUE).build();
    }

    @Bean
    public Binding notificationBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_LIKE);
    }
}
