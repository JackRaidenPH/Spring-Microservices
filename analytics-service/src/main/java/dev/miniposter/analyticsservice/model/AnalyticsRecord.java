package dev.miniposter.analyticsservice.model;

import dev.miniposter.analyticsservice.dto.AnalyticsRecordDTO.DenyReason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
//JPA
@Entity
@Table(name = "analytics")
public class AnalyticsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long authorId;

    @Column(nullable = false)
    private boolean accepted;

    @Column
    @Builder.Default
    private String denyReason = DenyReason.NONE.name();

    @Column
    @Builder.Default
    private String optionalDetails = "";
}
