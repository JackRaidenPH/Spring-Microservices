package dev.miniposter.analyticsservice.dto;

import java.io.Serializable;

public record AnalyticsRecordDTO(Long authorID, boolean accepted, String reason, String optionalDetails) implements Serializable {
    public enum DenyReason {
        NONE,
        BAD_WORDS,
        BAD_LENGTH
    }
}
