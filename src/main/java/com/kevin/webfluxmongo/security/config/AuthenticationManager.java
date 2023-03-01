package com.kevin.webfluxmongo.security.config;

import com.kevin.webfluxmongo.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtProvider.getUserNameFromToken(authToken);
        return Mono.just(jwtProvider.validateToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> {
                    Claims claims = jwtProvider.getAllClaimsFromToken(authToken);
                    List<String> roles = claims.get("roles", List.class);
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            roles.stream().map( SimpleGrantedAuthority::new ).collect(Collectors.toList())
                    );
                });
    }
}
