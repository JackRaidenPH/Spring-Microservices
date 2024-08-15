package dev.miniposter.gateway.dto;


import java.io.Serializable;

public record PostDTO(Long userId, String contents) implements Serializable {

}