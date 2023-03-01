package com.kevin.webfluxmongo.handler;

import com.kevin.webfluxmongo.documents.Note;
import com.kevin.webfluxmongo.dto.ResponseDTO;
import com.kevin.webfluxmongo.dto.SaveNoteDTO;
import com.kevin.webfluxmongo.service.NoteService;
import com.kevin.webfluxmongo.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class NoteHandler {

    private final NoteService service;
    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> create(ServerRequest req ){
        String userId = req.pathVariable("userId");
        Mono<SaveNoteDTO> note = req.bodyToMono(SaveNoteDTO.class).doOnNext(objectValidator::validate);
        return note.flatMap( n -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body( service.create( n, userId ) , Note.class));
    }

    public Mono<ServerResponse> getByUser(ServerRequest req){
        String userId = req.pathVariable("userId");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(service.getByUserId(userId), Note.class);
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String userId = req.pathVariable("userId");
        String noteId = req.pathVariable("noteId");
        Mono<SaveNoteDTO> note = req.bodyToMono(SaveNoteDTO.class).doOnNext(objectValidator::validate);
        return note.flatMap( n -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(service.update(n, noteId, userId), Note.class));
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String noteId = req.pathVariable("noteId");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body( service.delete(noteId), ResponseDTO.class);
    }

}
