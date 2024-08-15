package dev.miniposter.analyticsservice.configuration;

import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.queue.analyticsQueue:addAnalyticsRecordQueue}")
    private String ADD_ANALYTICS_RECORD_QUEUE;

    @Bean
    public Queue analyticsQueue() {
        return new Queue(ADD_ANALYTICS_RECORD_QUEUE, false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
