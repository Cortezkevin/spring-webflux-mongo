package com.kevin.webfluxmongo.handler;

import com.kevin.webfluxmongo.documents.User;
import com.kevin.webfluxmongo.dto.*;
import com.kevin.webfluxmongo.security.jwt.JwtProvider;
import com.kevin.webfluxmongo.service.EmailService;
import com.kevin.webfluxmongo.service.UserService;
import com.kevin.webfluxmongo.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final ObjectValidator validator;
    private final UserService service;
    private final EmailService emailService;

    public Mono<ServerResponse> getUserFromToken(ServerRequest req){
        String header = req.headers().header("Authorization").get(0);
        String token = header.substring(7);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body( service.getUserFromToken(token), User.class);
    }

    public Mono<ServerResponse> create(ServerRequest req){
        Mono<NewUserDTO> newUserDTOMono = req.bodyToMono(NewUserDTO.class).doOnNext(validator::validate);
        return  newUserDTOMono.flatMap( newUserDTO -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body( service.create(newUserDTO), ResponseDTO.class ) );
    }

    public Mono<ServerResponse> login( ServerRequest req ){
        Mono<LoginUserDTO> loginUserDTOMono = req.bodyToMono(LoginUserDTO.class).doOnNext(validator::validate);
        return loginUserDTOMono.flatMap( loginUserDTO -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body( service.login( loginUserDTO ), ResponseDTO.class ) );
    }

    public Mono<ServerResponse> changePassword( ServerRequest request){
        Mono<ChangePasswordDTO> changePasswordDTOMono = request.bodyToMono(ChangePasswordDTO.class).doOnNext(validator::validate);
        return changePasswordDTOMono.flatMap( dto ->  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body( service.changePassword( dto ), ResponseDTO.class ));
    }

    public Mono<ServerResponse> sendEmail(ServerRequest request){
        String to = request.pathVariable("to");
        return emailService.sendHtmlTemplateEmail( to )
                .flatMap( message ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(new ResponseDTO(message, null)), ResponseDTO.class)
                );
    }

}
