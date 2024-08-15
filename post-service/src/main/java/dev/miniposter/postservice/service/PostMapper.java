package dev.miniposter.postservice.service;

import dev.miniposter.postservice.dto.PostDTO;
import dev.miniposter.postservice.model.Post;

public final class PostMapper {

    public static PostDTO entityToDTO(Post post) {
        return new PostDTO(
                post.getUserId(),
                post.getContents()
        );
    }

    public static Post dtoToPost(PostDTO post) {
        return Post.builder()
                .userId(post.userId())
                .contents(post.contents())
                .build();
    }

}
