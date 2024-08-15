package dev.miniposter.analyticsservice.service;

import dev.miniposter.analyticsservice.configuration.RabbitMQConfiguration;
import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO;
import dev.miniposter.analyticsservice.model.AnalyticsRecord;
import dev.miniposter.analyticsservice.repository.AnalyticsRepository;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log
@Service
public class AnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @RabbitListener(queues = "${rabbitmq.queue.analyticsQueue:addAnalyticsRecordQueue}")
    public void listen(AnalyticsRecordDTO message) {
        try {
            AnalyticsRecord saved = this.analyticsRepository.save(AnalyticsRecordMapper.dtoToEntity(message));
            log.info("Saved new analytics record with ID: " + saved.getId());
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
        }
    }
}
