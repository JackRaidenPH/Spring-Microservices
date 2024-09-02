package dev.miniposter.filterservice.service;

import dev.miniposter.filterservice.model.Filter;
import dev.miniposter.filterservice.repository.FilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class FilterService {

    private final FilterRepository filterRepository;

    public Set<String> tokenizeText(String text) {
        return Arrays.stream(text.replaceAll("[^\\w'` ]", " ").split(" "))
                .filter(Predicate.not(String::isBlank))
                .map(String::toLowerCase)
                .map(String::strip)
                .collect(Collectors.toSet());
    }

    public Set<String> checkTextHavingBadWords(String text) {
        Set<String> wordsInText = this.tokenizeText(text);
        return this.filterRepository.findByFilterIn(wordsInText).stream()
                .map(Filter::getFilter)
                .collect(Collectors.toSet());
    }

    public Set<Filter> findBadWordsInSet(Set<String> words) {
        return this.filterRepository.findByFilterIn(words);
    }

    public boolean isBadWord(String text) {
        return this.filterRepository.findByFilter(text).isPresent();
    }

    public List<Filter> getAllFilters() {
        return this.filterRepository.findAll();
    }

    public boolean addBadWord(String word) {
        if (word == null || word.isBlank()) {
            return false;
        }

        Filter filter = Filter.builder().filter(word).build();
        this.filterRepository.save(filter);
        log.info("Saved new bad word: " + word);
        return true;
    }

    public boolean removeBadWord(String badWord) {
        Optional<Filter> filter = this.filterRepository.findByFilter(badWord);
        if (filter.isPresent()) {
            this.filterRepository.delete(filter.get());
            return true;
        } else {
            return false;
        }
    }
}
