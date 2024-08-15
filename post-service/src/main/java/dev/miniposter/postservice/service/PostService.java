package dev.miniposter.postservice.service;

import dev.miniposter.postservice.configuration.RabbitMQConfiguration;
import dev.miniposter.postservice.dto.AnalyticsRecordDTO;
import dev.miniposter.postservice.dto.AnalyticsRecordDTO.DenyReason;
import dev.miniposter.postservice.dto.PostDTO;
import dev.miniposter.postservice.model.Post;
import dev.miniposter.postservice.repository.PostRepository;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

//Lombok
@Log
//Spring
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RabbitMQService rabbitMQService;

    private final RestClient webClient;


    private final int maxLength;

    public PostService(
            @Value("${rest.checkBadWordsURL}") String filterUrl,
            @Value("${post.maxLength:200}") int maxLength,
            RestClient.Builder webClientBuilder
    ) {
        this.webClient = webClientBuilder.baseUrl(filterUrl).build();
        this.maxLength = maxLength;
    }

    public List<String> checkWords(String text) {
        return this.webClient.method(HttpMethod.GET)
                .uri("/check_text")
                .body(text)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

    }

    private void declinePost(Post post, DenyReason reason, String details) {
        AnalyticsRecordDTO analyticsRecordDTO = new AnalyticsRecordDTO(
                post.getUserId(),
                false,
                reason.name(),
                details
        );
        this.rabbitMQService.sendAnalyticsRecord(analyticsRecordDTO);
        log.info("Sent analytics report for declined post" +
                ", Reason: " + reason +
                ", Details: " + details
        );
    }

    private void acceptPost(Post post) {
        AnalyticsRecordDTO analyticsRecordDTO = new AnalyticsRecordDTO(post.getUserId());
        this.rabbitMQService.sendAnalyticsRecord(analyticsRecordDTO);
        log.info("Sent analytics report for accepted post with ID: " + post.getPostId());
    }

    public void savePost(Post post) {
        try {
            String postText = post.getContents();

            if (postText.length() > this.maxLength) {
                this.declinePost(
                        post,
                        DenyReason.BAD_LENGTH,
                        String.valueOf(postText.length())
                );
                return;
            }

            List<String> badWords = this.checkWords(postText);

            if (badWords == null) {
                this.declinePost(
                        post,
                        DenyReason.NONE,
                        ""
                );
                return;
            } else if (!badWords.isEmpty()) {
                this.declinePost(
                        post,
                        DenyReason.BAD_WORDS,
                        String.join(",", badWords)
                );
                return;
            }

            Post saved = this.postRepository.save(post);
            this.acceptPost(saved);
            log.info("Saved new post with ID = " + saved.getPostId());
        } catch (Exception e) {
            log.warning(e.getLocalizedMessage());
            throw e;
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.postQueue:addPostQueue}")
    public void listen(PostDTO postMessage) {
        try {
            Post post = PostMapper.dtoToPost(postMessage);
            this.savePost(post);
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
        }
    }
}
