package com.kevin.webfluxmongo.service;

import com.kevin.webfluxmongo.documents.User;
import com.kevin.webfluxmongo.dto.LoginUserDTO;
import com.kevin.webfluxmongo.dto.NewUserDTO;
import com.kevin.webfluxmongo.dto.ResponseDTO;
import com.kevin.webfluxmongo.dto.UpdateUserDTO;
import com.kevin.webfluxmongo.exception.CustomException;
import com.kevin.webfluxmongo.repository.UserRepository;
import com.kevin.webfluxmongo.security.enums.RolName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public Flux<User> getAll(){
        return repository.findAll();
    }

    public Mono<User> getById( String id ){
        Mono<User> findUser = repository.findById( id );
        Mono<Boolean> existsUser = findUser.hasElement();

        return existsUser.flatMap( exists ->
                    exists
                    ? findUser
                    : Mono.error( new CustomException( HttpStatus.NOT_FOUND ,"User not found with id " + id ))
                );
    }

    public Mono<User> create( NewUserDTO u){
        List<RolName> newUserRoles = new ArrayList<>();
        if( u.getRoles() != null && u.getRoles().contains("admin")){
            newUserRoles.add(RolName.ROLE_ADMIN);
        }
        newUserRoles.add(RolName.ROLE_USER);
        User newUser = User.builder().username(u.getUsername()).email(u.getEmail()).password(u.getPassword()).roles(newUserRoles).build();
        System.out.println(newUser.toString());
        return repository.save( newUser );
    }

    public Mono<ResponseDTO> login(LoginUserDTO user){
        Mono<Boolean> existsUsername = repository.existsByUsername(user.getUsername());
        return existsUsername.flatMap( exists ->
                    exists
                    ? repository.findByUsername(user.getUsername()).flatMap( u -> {
                        if( u.getPassword().equals(user.getPassword())){
                            return Mono.just(new ResponseDTO("User logged successfully", u));
                        }
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Password is invalid"));
                    })
                    : Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Username is invalid"))
                );
    }

    public Mono<User> update( UpdateUserDTO u, String id ){
        return repository.findById( id )
                .flatMap( userFound ->
                            repository.existsByUsername( u.getUsername() )
                                    .flatMap( existsUsername -> {
                                        if( existsUsername ){
                                            return Mono.error( new CustomException( HttpStatus.BAD_REQUEST ,"User exists with username: " + u.getUsername() ));
                                        }
                                        return repository.existsByEmail( u.getEmail() );
                                    }).flatMap( existsEmail -> {
                                        if( existsEmail ){
                                            return Mono.error( new CustomException( HttpStatus.BAD_REQUEST ,"User exists with email: " + u.getEmail() ));
                                        }
                                        userFound.setUsername( u.getUsername() != null ? u.getUsername() : userFound.getUsername() );
                                        userFound.setEmail( u.getEmail() != null ? u.getEmail() : userFound.getEmail() );
                                        userFound.setPassword( u.getPassword() != null ? passwordEncoder.encode(u.getPassword()) : userFound.getPassword() );

                                        List<RolName> roles = new ArrayList<>();
                                        if( u.getRoles() != null ){
                                            roles.add(RolName.ROLE_USER);
                                            if( u.getRoles().contains("admin")){
                                                roles.add(RolName.ROLE_ADMIN);
                                            }
                                        }

                                        userFound.setRoles( u.getRoles() != null ? roles : userFound.getRoles());
                                        return repository.save( userFound );
                                    }))
                .switchIfEmpty( Mono.error( new CustomException( HttpStatus.NOT_FOUND ,"User not found with id " + id )) );
    }

    public Mono<Void> delete( String id ){
        return repository.findById( id ).hasElement()
                .flatMap( existsUser ->
                        existsUser
                                ? repository.deleteById( id )
                                : Mono.error( new CustomException( HttpStatus.NOT_FOUND ,"User not found with id " + id )));
    }

    public Mono<User> getByUsername(String username){
        return repository.findByUsername(username);
    }

}
