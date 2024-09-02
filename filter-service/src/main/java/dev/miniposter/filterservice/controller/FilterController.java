package dev.miniposter.filterservice.controller;


import dev.miniposter.filterservice.model.Filter;
import dev.miniposter.filterservice.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class FilterController {

    private final FilterService filterService;

    @GetMapping("/check_text")
    public ResponseEntity<Set<String>> getPosts(@RequestBody String text) {
        try {
            Set<String> wordsFound = this.filterService.checkTextHavingBadWords(text);
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
                result = !this.filterService.isBadWord(word);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bad_words")
    public ResponseEntity<List<String>> badWordsList() {
        try {
            List<String> badWords = this.filterService.getAllFilters().stream()
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
            this.filterService.addBadWord(badWord);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/remove_bad_word")
    public ResponseEntity<Void> removeBadWord(@RequestParam String badWord) {
        try {
            return this.filterService.removeBadWord(badWord)
                    ? ResponseEntity.accepted().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
