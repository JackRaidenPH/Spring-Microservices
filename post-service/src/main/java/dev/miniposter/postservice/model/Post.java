package dev.miniposter.postservice.model;

import jakarta.persistence.*;
import lombok.*;

//JPA
@Entity
//Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String contents;
}
