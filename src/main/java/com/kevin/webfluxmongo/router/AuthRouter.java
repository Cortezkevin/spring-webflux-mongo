package com.kevin.webfluxmongo.router;

import com.kevin.webfluxmongo.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AuthRouter {

    private final static String PATH = "auth";

    @Bean
    public RouterFunction<ServerResponse> authRoute(AuthHandler authHandler){
        return RouterFunctions.route()
                .POST(PATH + "/login", authHandler::login)
                .POST(PATH + "/register", authHandler::create)
                .GET(PATH + "/verifyToken/{token}", authHandler::verifyToken)
                .GET(PATH + "/sendEmail/{to}", authHandler::sendEmail)
                .POST(PATH + "/changePassword", authHandler::changePassword)
                .build();
    }

}
