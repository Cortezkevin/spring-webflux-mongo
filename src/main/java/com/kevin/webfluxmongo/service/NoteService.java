package com.kevin.webfluxmongo.service;

import com.kevin.webfluxmongo.documents.Note;
import com.kevin.webfluxmongo.documents.User;
import com.kevin.webfluxmongo.dto.ResponseDTO;
import com.kevin.webfluxmongo.dto.SaveNoteDTO;
import com.kevin.webfluxmongo.exception.CustomException;
import com.kevin.webfluxmongo.repository.NoteRepository;
import com.kevin.webfluxmongo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository repository;
    private final UserRepository userRepository;

    public Mono<Note> create( SaveNoteDTO n, String userId){
        return userRepository.findById( userId )
                .flatMap( user -> repository.save( Note.builder()
                        .createdAt(new Date().getTime())
                        .userId(userId)
                        .title(n.getTitle())
                        .body(n.getBody())
                        .build())
                )
                .switchIfEmpty( Mono.error(new CustomException( HttpStatus.NOT_FOUND ,"User not found with id "+ userId)) );
    }

    public Mono<Note> update(SaveNoteDTO n, String noteId, String userId){
        return userRepository.findById( userId )
                .flatMap( user -> repository.findById( noteId )
                        .flatMap( note ->  {
                            note.setTitle(n.getTitle() != null ? n.getTitle() : note.getTitle() );
                            note.setBody(n.getBody() != null ? n.getBody() : note.getBody() );
                            note.setUpdateAt( new Date().getTime());
                            return repository.save( note );
                        })
                        .switchIfEmpty( Mono.error(new CustomException( HttpStatus.NOT_FOUND ,"Note not found with id "+ noteId)) )
                )
                .switchIfEmpty( Mono.error(new CustomException( HttpStatus.NOT_FOUND ,"User not found with id "+ userId)) );
    }

    public Flux<Note> getByUserId(String userId){
        return userRepository.findById( userId )
                .flatMapMany( user -> repository.findByUserId( user.getId() ) )
                .switchIfEmpty( Mono.error(new CustomException( HttpStatus.NOT_FOUND ,"User not found with id "+ userId)) );
    }

    public Mono<ResponseDTO> delete(String noteId){
        return repository.findById( noteId )
                .flatMap( note -> repository.deleteById( note.getId() ).then( Mono.just( new ResponseDTO("Note deleted successfully", null) ) ) )
                .switchIfEmpty( Mono.error(new CustomException( HttpStatus.NOT_FOUND ,"Note not found with id "+ noteId)) );
    }

}