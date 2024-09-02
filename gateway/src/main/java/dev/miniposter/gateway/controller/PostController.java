package dev.miniposter.gateway.controller;

import dev.miniposter.gateway.configuration.RabbitMQConfiguration;
import dev.miniposter.gateway.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/post")
@Log
@RequiredArgsConstructor
public class PostController {

    private final RabbitMQConfiguration rabbitMQConfiguration;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/add")
    public ResponseEntity<Void> sendPost(@RequestBody PostDTO post) {
        try {
            rabbitTemplate.convertAndSend(this.rabbitMQConfiguration.postExchange().getName(), "post.add", post);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
