package dev.miniposter.filterservice.controller;


import dev.miniposter.filterservice.model.Filter;
import dev.miniposter.filterservice.repository.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
public class FilterController {

    @Autowired
    private FilterRepository filterRepository;

    @GetMapping("/check_text")
    public ResponseEntity<List<String>> getPosts(@RequestBody String text) {
        try {
            Set<String> wordsInText = Arrays.stream(text.replaceAll("[^\\w'` ]", " ").split(" "))
                    .filter(Predicate.not(String::isBlank))
                    .map(String::toLowerCase)
                    .map(String::strip)
                    .collect(Collectors.toSet());

            List<String> wordsFound = this.filterRepository.findByFilterIn(wordsInText).stream()
                    .map(Filter::getFilter)
                    .toList();
            return ResponseEntity.ok(wordsFound);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/is_bad")
    public ResponseEntity<Boolean> isBad(@RequestParam String word) {
        try {
            boolean result = false;
            if (word != null) {
                result = !this.filterRepository.findByFilterIn(Set.of(word)).isEmpty();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bad_words")
    public ResponseEntity<List<String>> badWordsList() {
        try {
            List<String> badWords = this.filterRepository.findAll().stream()
                    .map(Filter::getFilter)
                    .toList();
            return ResponseEntity.ok(badWords);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add_bad_word")
    public ResponseEntity<Void> addBadWord(@RequestParam String badWord) {
        try {
            Filter filter = Filter.builder().filter(badWord).build();
            this.filterRepository.save(filter);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/remove_bad_word")
    public ResponseEntity<Void> removeBadWord(@RequestParam String badWord) {
        try {
            Optional<Filter> filter = this.filterRepository.findByFilter(badWord);
            if (filter.isPresent()) {
                this.filterRepository.delete(filter.get());
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
