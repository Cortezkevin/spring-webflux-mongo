package com.kevin.webfluxmongo.router;

import com.kevin.webfluxmongo.handler.UserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class UserRouter {

    private static final String PATH = "user";

    @Bean
    RouterFunction<ServerResponse> userRoute(UserHandler handler ){
        return RouterFunctions.route()
                .GET("/user", handler::getAll)
                .GET(PATH + "/{id}", handler::getById)
                .PUT(PATH + "/{id}", handler::update)
                .DELETE(PATH + "/{id}", handler::delete)
                .build();
    }

}

