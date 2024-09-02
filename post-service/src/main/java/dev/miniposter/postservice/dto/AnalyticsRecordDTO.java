package dev.miniposter.postservice.dto;

import java.io.Serializable;

public record AnalyticsRecordDTO(Long authorID, boolean accepted, String reason, String optionalDetails) implements Serializable {

    public AnalyticsRecordDTO(Long authorID) {
        this(authorID, true, DenyReason.NONE.name(), "");
    }

    public enum DenyReason {
        NONE,
        BAD_WORDS,
        BAD_LENGTH
    }
}
