package dev.miniposter.gateway.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.queue.postQueue:addPostQueue}")
    private String ADD_POST_QUEUE;

    @Value("${rabbitmq.queue.analyticsQueue:addAnalyticsRecordQueue}")
    private String POST_EXCHANGE;

    @Bean
    public Queue postQueue() {
        return new Queue(ADD_POST_QUEUE, false);
    }

    @Bean
    public Exchange postExchange() {
        return new DirectExchange(POST_EXCHANGE, true, false);
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("post.add").noargs();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
