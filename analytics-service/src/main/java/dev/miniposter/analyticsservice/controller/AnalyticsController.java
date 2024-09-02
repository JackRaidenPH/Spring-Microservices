package dev.miniposter.analyticsservice.controller;

import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO;
import dev.miniposter.analyticsservice.dto.BadWordsSummaryDTO;
import dev.miniposter.analyticsservice.dto.StatisticsSummaryDTO;
import dev.miniposter.analyticsservice.dto.UserStatisticsSummaryDTO;
import dev.miniposter.analyticsservice.model.AnalyticsRecord;
import dev.miniposter.analyticsservice.service.AnalyticsRecordMapper;
import dev.miniposter.analyticsservice.service.AnalyticsService;
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

    private final AnalyticsService analyticsService;

    @GetMapping("/record/{id}")
    ResponseEntity<AnalyticsRecordDTO> getRecordDetails(@PathVariable("id") long recordId) {
        try {
            Optional<AnalyticsRecord> record = this.analyticsService.findById(recordId);
            return record
                    .map(analyticsRecord -> ResponseEntity.ok(AnalyticsRecordMapper.entityToDTO(analyticsRecord)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    ResponseEntity<UserStatisticsSummaryDTO> getUserStatistics(@PathVariable("id") long userId) {
        try {
            List<AnalyticsRecord> records = this.analyticsService.findAllByAuthorId(userId);
            Pair<Long, Long> approvedRejected = this.analyticsService.calculateApprovedRejected(records);
            return ResponseEntity.ok(new UserStatisticsSummaryDTO(
                    approvedRejected.getFirst(),
                    approvedRejected.getSecond()
            ));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/summary")
    ResponseEntity<StatisticsSummaryDTO> getSummary() {
        try {
            List<AnalyticsRecord> records = this.analyticsService.findAll();
            Pair<Long, Long> approvedRejected = this.analyticsService.calculateApprovedRejected(records);
            return ResponseEntity.ok(new StatisticsSummaryDTO(
                    records.size(),
                    approvedRejected.getFirst(),
                    approvedRejected.getSecond()
            ));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bad_words_summary")
    ResponseEntity<BadWordsSummaryDTO> getBadWordsSummary() {
        try {
            Map<String, Long> stats = this.analyticsService.countBadWordsUsages();
            return ResponseEntity.ok(new BadWordsSummaryDTO(stats));
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
