package dev.miniposter.analyticsservice.repository;

import dev.miniposter.analyticsservice.model.AnalyticsRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalyticsRepository extends JpaRepository<AnalyticsRecord, Long> {
    List<AnalyticsRecord> findAllByAuthorId(long uuid);

    long countByAuthorId(long uuid);

    long countByAccepted(boolean accepted);

    long countByAuthorIdAndAccepted(long uuid, boolean accepted);

    List<AnalyticsRecord> findAllByDenyReason(String denyReason);
}
