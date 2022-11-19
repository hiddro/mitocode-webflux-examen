package com.examen.edu.handler;

import com.examen.edu.models.Registration;
import com.examen.edu.service.RegistrationService;
import com.examen.edu.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class RegistrationHandler {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RequestValidator requestValidator;

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(registrationService.findAll(), Registration.class);
    }

    public Mono<ServerResponse> create(ServerRequest req){
        Mono<Registration> courseMono = req.bodyToMono(Registration.class);

        return courseMono
                .flatMap(requestValidator::validate)
                .flatMap(registrationService::save)
                .flatMap(registration -> ServerResponse
                        .created(URI.create(req.uri().toString().concat("/").concat(registration.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(registration)));
    }
}
