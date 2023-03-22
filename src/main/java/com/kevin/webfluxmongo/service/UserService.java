package com.kevin.webfluxmongo.service;

import com.kevin.webfluxmongo.documents.User;
import com.kevin.webfluxmongo.dto.*;
import com.kevin.webfluxmongo.exception.CustomException;
import com.kevin.webfluxmongo.repository.UserRepository;
import com.kevin.webfluxmongo.security.enums.RolName;
import com.kevin.webfluxmongo.security.jwt.JwtProvider;
import com.kevin.webfluxmongo.security.model.MainUser;
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
    private final JwtProvider jwtProvider;

    public Flux<User> getAll(){
        return repository.findAll();
    }

    public Mono<User> getById( String id ){
        return repository.findById( id )
                .switchIfEmpty( Mono.error( new CustomException( HttpStatus.NOT_FOUND ,"User not found with id " + id )) );
    }

    public Mono<User> getUserFromToken(String token){
         String username = jwtProvider.getUserNameFromToken( token );
         return repository.findByUsername( username );
    }

    public Mono<ResponseDTO> create( NewUserDTO u){
        List<RolName> newUserRoles = new ArrayList<>();
        return repository.existsByUsername( u.getUsername() ).flatMap( existsUsername ->
                !existsUsername
                        ? repository.existsByEmail( u.getEmail() ).flatMap( existsEmail -> {
                            if( existsEmail ) return Mono.error( new CustomException( HttpStatus.BAD_REQUEST ,u.getEmail() + " already in use"));
                            if( u.getRoles() != null && u.getRoles().contains("admin")){
                                newUserRoles.add(RolName.ROLE_ADMIN);
                            }
                            newUserRoles.add(RolName.ROLE_USER);
                            User newUser = User.builder()
                                    .username(u.getUsername())
                                    .email(u.getEmail())
                                    .password(passwordEncoder.encode(u.getPassword()))
                                    .roles(newUserRoles).build();
                            return repository.save( newUser ).map( user -> new ResponseDTO( "User Registered Successfully", newUser ) );
                        })
                        : Mono.error( new CustomException( HttpStatus.BAD_REQUEST ,u.getUsername() + " already in use")));
    }

    public Mono<ResponseDTO> login(LoginUserDTO loginUserDTO){
        return repository.findByUsername(loginUserDTO.getUsername()).flatMap( user -> {
                    if( !passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Invalid Credentials"));
                    String token = jwtProvider.generateToken( MainUser.build( user ) );
                    JwtDTO jwtDTO = new JwtDTO( token, user );
                    return Mono.just(new ResponseDTO("User logged successfully", jwtDTO));
                })
                .switchIfEmpty( Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Invalid Credentials")) );
    }

    public Mono<ResponseDTO> changePassword(ChangePasswordDTO dto){
        return repository.findByTokenPassword( dto.getTokenPassword() )
                .flatMap( user -> {
                    if( dto.getPassword().equals( dto.getConfirmPassword())){
                        user.setPassword( passwordEncoder.encode( dto.getPassword()) );
                        user.setTokenPassword( null );
                        return repository.save( user ).flatMap( userUpdated -> Mono.just( new ResponseDTO("Password updated", null) ));
                    }else {
                        return Mono.error( new CustomException(HttpStatus.BAD_REQUEST, "The passwords not matches"));
                    }
                })
                .switchIfEmpty( Mono.error( new CustomException(HttpStatus.BAD_REQUEST, "The token is not valid")));
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

    public Mono<ResponseDTO> delete( String id ){
        return repository.findById( id )
                .flatMap( user -> repository.deleteById( user.getId() ).then( Mono.just( new ResponseDTO("User delted successfully", null)) ))
                .switchIfEmpty( Mono.error( new CustomException( HttpStatus.NOT_FOUND ,"User not found with id " + id )) );
    }

    public Mono<User> getByUsername(String username){
        return repository.findByUsername(username);
    }

}
