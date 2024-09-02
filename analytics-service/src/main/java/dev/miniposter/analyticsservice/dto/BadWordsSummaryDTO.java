package dev.miniposter.analyticsservice.dto;

import java.util.Map;

public record BadWordsSummaryDTO(Map<String, Long> wordCountPairs) {
}
