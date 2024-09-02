package dev.miniposter.filterservice.repository;


import dev.miniposter.filterservice.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilterRepository extends JpaRepository<Filter, Long> {
    Set<Filter> findByFilterIn(Set<String> words);
    Optional<Filter> findByFilter(String word);
}