package com.toie.tutorialRabbitMq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@AllArgsConstructor
public class RabbitMqConfig {
    public final static String QUEUE = "tutorialQueue";
    public final static String EXCHANGE = "tutorialQueue";
    public final static String ROUTING_KEY = "tutorial";


    @Bean
    public Exchange tutorialExchange() {
        final var exchange = new TopicExchange(EXCHANGE);
        exchange.setShouldDeclare(true);
        return exchange;
    }

    @Bean
    public Queue tutorialQueue() {
        return QueueBuilder.durable(QUEUE)
                .quorum()
                .deadLetterExchange(QUEUE + ".dlq")
                .deadLetterRoutingKey("deadLetter")
                .build();
    }

    @Bean
    public Binding tutorialQueueBinding(final Exchange tutorialExchange, final Queue tutorialQueue) {
        return BindingBuilder.bind(tutorialQueue).to(tutorialExchange)
                .with(ROUTING_KEY).noargs();
    }

    @Bean
    public Exchange tutorialDeadLetterExchange() {
        return new DirectExchange(EXCHANGE + ".dlx");
    }

    @Bean
    public Queue tutorialDeadLetterQueue() {
        return QueueBuilder.durable(QUEUE + ".dlq")
                .quorum()
                .build();
    }

    @Bean
    public Binding tutorialDeadLetterQueueBinding(final Exchange tutorialDeadLetterExchange, final Queue tutorialDeadLetterQueue) {
        return BindingBuilder.bind(tutorialDeadLetterQueue).to(tutorialDeadLetterExchange).with("deadLetter").noargs();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }
}
