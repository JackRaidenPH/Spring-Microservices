package dev.miniposter.analyticsservice.service;

import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO;
import dev.miniposter.analyticsservice.model.AnalyticsRecord;

public class AnalyticsRecordMapper {

    public static AnalyticsRecordDTO entityToDTO(AnalyticsRecord record) {
        return new AnalyticsRecordDTO(
                record.getAuthorId(),
                record.isAccepted(),
                record.getDenyReason(),
                record.getOptionalDetails()
        );
    }

    public static AnalyticsRecord dtoToEntity(AnalyticsRecordDTO dto) {
        return AnalyticsRecord.builder()
                .authorId(dto.authorID())
                .accepted(dto.accepted())
                .denyReason(dto.reason())
                .optionalDetails(dto.optionalDetails())
                .build();
    }
}
