package dev.miniposter.postservice.controller;

import dev.miniposter.postservice.dto.PostDTO;
import dev.miniposter.postservice.model.Post;
import dev.miniposter.postservice.repository.PostRepository;
import dev.miniposter.postservice.service.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @GetMapping("/posts/all")
    public ResponseEntity<List<PostDTO>> getPosts() {
        try {
            List<PostDTO> posts = this.postRepository.findAll().stream()
                    .map(PostMapper::entityToDTO)
                    .toList();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") long id) {
        try {
            Optional<Post> posts = this.postRepository.findById(id);
            return posts
                    .map(p -> ResponseEntity.ok(PostMapper.entityToDTO(p)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/user/{id}")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable("id") long id) {
        try {
            List<PostDTO> posts = this.postRepository.findAllByUserId(id).stream()
                    .map(PostMapper::entityToDTO)
                    .toList();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
