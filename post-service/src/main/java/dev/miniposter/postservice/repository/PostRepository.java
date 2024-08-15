package dev.miniposter.postservice.repository;

import dev.miniposter.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(long uuid);
    List<Post> findAllByContentsIgnoreCaseContaining(String word);
}