package dev.miniposter.analyticsservice.service;

import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO;
import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO.DenyReason;
import dev.miniposter.analyticsservice.model.AnalyticsRecord;
import dev.miniposter.analyticsservice.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    @RabbitListener(queues = "${rabbitmq.queue.analyticsQueue:addAnalyticsRecordQueue}")
    public void listen(AnalyticsRecordDTO message) {
        try {
            AnalyticsRecord saved = this.analyticsRepository.save(AnalyticsRecordMapper.dtoToEntity(message));
            log.info("Saved new analytics record with ID: " + saved.getId());
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
        }
    }

    public Pair<Long, Long> calculateApprovedRejected(List<AnalyticsRecord> records) {
        long approved = records.stream().filter(AnalyticsRecord::isAccepted).count();
        long rejected = records.size() - approved;
        return Pair.of(approved, rejected);
    }

    public Optional<AnalyticsRecord> findById(long id) {
        return this.analyticsRepository.findById(id);
    }

    public List<AnalyticsRecord> findAllByAuthorId(long authorId) {
        return this.analyticsRepository.findAllByAuthorId(authorId);
    }

    public List<AnalyticsRecord> findAll() {
        return this.analyticsRepository.findAll();
    }

    public Map<String, Long> countBadWordsUsages() {
        List<AnalyticsRecord> records = this.analyticsRepository.findAllByDenyReason(DenyReason.BAD_WORDS.name());
        return records.parallelStream()
                .map(AnalyticsRecord::getOptionalDetails)
                .map(details -> details.split("[ ,]+"))
                .flatMap(Arrays::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
