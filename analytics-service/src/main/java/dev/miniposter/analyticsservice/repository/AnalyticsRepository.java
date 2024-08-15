package dev.miniposter.analyticsservice.repository;

import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO.DenyReason;
import dev.miniposter.analyticsservice.model.AnalyticsRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface AnalyticsRepository extends JpaRepository<AnalyticsRecord, Long> {
    List<AnalyticsRecord> findAllByAuthorId(long uuid);

    long countByAuthorId(long uuid);

    long countByAccepted(boolean accepted);

    long countByAuthorIdAndAccepted(long uuid, boolean accepted);

    List<AnalyticsRecord> findAllByDenyReason(String denyReason);

    default Map<String, Long> countBadWords() {
        List<AnalyticsRecord> records = this.findAllByDenyReason(DenyReason.BAD_WORDS.name());

        return records.parallelStream()
                .map(AnalyticsRecord::getOptionalDetails)
                .map(details -> details.split("[ ,]+"))
                .flatMap(Arrays::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
