package dev.miniposter.filterservice.model;

import jakarta.persistence.*;
import lombok.*;

//JPA
@Entity
//Lombok
@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
@Builder
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long filterId;

    @Column(nullable = false)
    private String filter;
}
