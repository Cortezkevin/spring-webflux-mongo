package com.kevin.webfluxmongo.repository;

import com.kevin.webfluxmongo.documents.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsername(String username);
    Mono<User> findByEmail(String email);
    Mono<User> findByTokenPassword(String tokenPassword);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
}
