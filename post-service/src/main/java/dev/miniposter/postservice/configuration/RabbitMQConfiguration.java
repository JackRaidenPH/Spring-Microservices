package dev.miniposter.postservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.queue.postQueue:addPostQueue}")
    private String ADD_POST_QUEUE ;

    @Value("${rabbitmq.queue.analyticsQueue:addAnalyticsRecordQueue}")
    private String ADD_ANALYTICS_RECORD_QUEUE;

    @Value("${rabbitmq.exchange.analyticsExchange:miniposter.analytics}")
    private String ANALYTICS_EXCHANGE;

    @Value("${rabbitmq.exchange.postExchange:miniposter.post}")
    private String POST_EXCHANGE;

    @Bean
    public Queue analyticsQueue() {
        return new Queue(ADD_ANALYTICS_RECORD_QUEUE, false);
    }

    @Bean
    public Exchange analyticsExchange() {
        return new DirectExchange(ANALYTICS_EXCHANGE, true, false);
    }

    @Bean
    public Queue postQueue() {
        return new Queue(ADD_POST_QUEUE, false);
    }

    @Bean
    public Exchange postExchange() {
        return new DirectExchange(POST_EXCHANGE, true, false);
    }

    @Bean
    public Declarables bindings() {
        return new Declarables(
                BindingBuilder
                        .bind(analyticsQueue())
                        .to(analyticsExchange())
                        .with("analytics.add")
                        .noargs()       ,
                BindingBuilder
                        .bind(postQueue())
                        .to(postExchange())
                        .with("post.add")
                        .noargs()
        );
    }



    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
