package com.kevin.webfluxmongo.router;

import com.kevin.webfluxmongo.handler.NoteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class NoteRouter {

    private static final String PATH = "note";

    @Bean
    public WebProperties.Resources reesources() {
        return new WebProperties.Resources();
    }

    @Bean
    public RouterFunction<ServerResponse> noteRoute(NoteHandler handler ){
        return RouterFunctions.route()
                .GET(PATH + "/{userId}", handler::getByUser)
                .POST(PATH + "/{userId}", handler::create)
                .PUT(PATH + "/{noteId}/user/{userId}", handler::update)
                .DELETE(PATH + "/{noteId}", handler::delete)
                .build();
    }

}

