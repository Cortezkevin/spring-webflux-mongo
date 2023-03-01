package com.kevin.webfluxmongo.repository;

import com.kevin.webfluxmongo.documents.Note;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NoteRepository extends ReactiveCrudRepository<Note, String> {
    Flux<Note> findByUserId(String userId);
}
