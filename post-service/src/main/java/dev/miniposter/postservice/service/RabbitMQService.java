package dev.miniposter.postservice.service;

import dev.miniposter.postservice.configuration.RabbitMQConfiguration;
import dev.miniposter.postservice.dto.AnalyticsRecordDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfiguration rabbitMQConfiguration;

    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAnalyticsRecord(@RequestBody AnalyticsRecordDTO post) {
        rabbitTemplate.convertAndSend(this.rabbitMQConfiguration.analyticsExchange().getName(), "analytics.add", post);
    }

}
