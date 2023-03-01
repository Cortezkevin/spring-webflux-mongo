package com.kevin.webfluxmongo.handler;

import com.kevin.webfluxmongo.dto.LoginUserDTO;
import com.kevin.webfluxmongo.dto.NewUserDTO;
import com.kevin.webfluxmongo.dto.ResponseDTO;
import com.kevin.webfluxmongo.exception.CustomException;
import com.kevin.webfluxmongo.dto.JwtDTO;
import com.kevin.webfluxmongo.security.jwt.JwtProvider;
import com.kevin.webfluxmongo.security.model.MainUser;
import com.kevin.webfluxmongo.service.UserService;
import com.kevin.webfluxmongo.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final ObjectValidator validator;
    private final UserService service;

    public Mono<ServerResponse> create(ServerRequest req){
        Mono<NewUserDTO> newUserDTOMono = req.bodyToMono(NewUserDTO.class).doOnNext(validator::validate);
        return newUserDTOMono.flatMap( newUserDTO -> {
            newUserDTO.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
            return service.create(newUserDTO).flatMap( newUser -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                    Mono.just(
                            new ResponseDTO("User Registered Successfully", newUser)
                    ),
                    ResponseDTO.class
            ));
        });
    }

    public Mono<ServerResponse> login( ServerRequest req ){
        Mono<LoginUserDTO> loginUserDTOMono = req.bodyToMono(LoginUserDTO.class).doOnNext(validator::validate);
        return loginUserDTOMono.flatMap( loginUserDTO -> service.getByUsername( loginUserDTO.getUsername() )
                .filter( userFind -> passwordEncoder.matches(loginUserDTO.getPassword(), userFind.getPassword()))
                .flatMap( userFind ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                                Mono.just(
                                        new ResponseDTO( "User Logged Successfully",
                                                new JwtDTO(jwtProvider.generateToken(MainUser.build( userFind )),
                                                        userFind))
                                ),
                                ResponseDTO.class)
                )
                .switchIfEmpty( Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Invalid Credentials")))
        );
    }

}
