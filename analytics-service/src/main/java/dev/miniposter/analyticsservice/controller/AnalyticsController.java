package dev.miniposter.analyticsservice.controller;

import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO;
import dev.miniposter.analyticsservice.model.AnalyticsRecord;
import dev.miniposter.analyticsservice.repository.AnalyticsRepository;
import dev.miniposter.analyticsservice.service.AnalyticsRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/analytics")
@Log
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsRepository analyticsRepository;

    @GetMapping("/record/{id}")
    ResponseEntity<AnalyticsRecordDTO> recordDetails(@PathVariable("id") long recordId) {
        try {
            Optional<AnalyticsRecord> record = this.analyticsRepository.findById(recordId);
            return record
                    .map(analyticsRecord -> ResponseEntity.ok(AnalyticsRecordMapper.entityToDTO(analyticsRecord)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    ResponseEntity<Map<String, Long>> userStatistics(@PathVariable("id") long userId) {
        try {
            List<AnalyticsRecord> records = this.analyticsRepository.findAllByAuthorId(userId);
            Pair<Long, Long> approvedRejected = this.calculateApprovedRejected(records);
            return ResponseEntity.ok(Map.of(
                    "approved", approvedRejected.getFirst(),
                    "rejected", approvedRejected.getSecond()
            ));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/summary")
    ResponseEntity<Map<String, Long>> statistics() {
        try {
            List<AnalyticsRecord> records = this.analyticsRepository.findAll();
            Pair<Long, Long> approvedRejected = this.calculateApprovedRejected(records);
            return ResponseEntity.ok(Map.of(
                    "total", (long) records.size(),
                    "approved", approvedRejected.getFirst(),
                    "rejected", approvedRejected.getSecond()
            ));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Pair<Long, Long> calculateApprovedRejected(List<AnalyticsRecord> records) {
        long approved = records.stream().filter(AnalyticsRecord::isAccepted).count();
        long rejected = records.size() - approved;
        return Pair.of(approved, rejected);
    }

    @GetMapping("/bad_words_summary")
    ResponseEntity<Map<String, Long>> allBadWords() {
        try {
            Map<String, Long> stats = this.analyticsRepository.countBadWords();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
