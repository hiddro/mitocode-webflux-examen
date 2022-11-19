package com.examen.edu.config;

import com.examen.edu.handler.StudentHandler;
import com.examen.edu.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routesDishes(StudentHandler studentHandler){
        return route(GET(Constants.URL_V2 + Constants.URL_STUDENT), studentHandler::findAll)
                .andRoute(GET(Constants.URL_V2 + Constants.URL_STUDENT + "/{id}"), studentHandler::findById)
                .andRoute(GET(Constants.URL_V2 + Constants.URL_STUDENT + Constants.STRING_ORDER + "/{key}"), studentHandler::getOrder)
                .andRoute(POST(Constants.URL_V2 + Constants.URL_STUDENT), studentHandler::create)
                .andRoute(PUT(Constants.URL_V2 + Constants.URL_STUDENT + "/{id}"), studentHandler::update)
                .andRoute(DELETE(Constants.URL_V2 + Constants.URL_STUDENT + "/{id}"), studentHandler::delete);
    }


}
