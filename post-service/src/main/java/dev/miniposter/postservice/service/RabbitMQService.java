package dev.miniposter.postservice.service;

import dev.miniposter.postservice.configuration.RabbitMQConfiguration;
import dev.miniposter.postservice.dto.AnalyticsRecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMQConfiguration rabbitMQConfiguration;

    public void sendAnalyticsRecord(@RequestBody AnalyticsRecordDTO post) {
        rabbitTemplate.convertAndSend(this.rabbitMQConfiguration.analyticsExchange().getName(), "analytics.add", post);
    }

}
