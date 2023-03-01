package com.kevin.webfluxmongo.handler;

import com.kevin.webfluxmongo.documents.User;
import com.kevin.webfluxmongo.dto.UpdateUserDTO;
import com.kevin.webfluxmongo.service.UserService;
import com.kevin.webfluxmongo.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserHandler {

    private final ObjectValidator validator;
    private final UserService service;

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> getAll( ServerRequest req ){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(service.getAll(), User.class);
    }

    public Mono<ServerResponse> getById( ServerRequest req ) {
        String id = req.pathVariable("id");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(service.getById(id), User.class);
    }

    public Mono<ServerResponse> update( ServerRequest req ){
        String id = req.pathVariable("id");
        Mono<UpdateUserDTO> user = req.bodyToMono(UpdateUserDTO.class).doOnNext( validator::validate );
        return user.flatMap( u -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body( service.update( u, id ) , User.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> delete( ServerRequest req ){
        String id = req.pathVariable("id");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(service.delete(id), User.class);
    }

}
