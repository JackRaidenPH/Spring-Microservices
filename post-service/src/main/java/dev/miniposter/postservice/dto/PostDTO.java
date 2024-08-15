package dev.miniposter.postservice.dto;

import java.io.Serializable;

public record PostDTO(Long userId, String contents) implements Serializable {

}
