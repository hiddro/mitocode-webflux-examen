package com.examen.edu.controller;

import com.examen.edu.models.Registration;
import com.examen.edu.service.RegistrationService;
import com.examen.edu.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(Constants.URL_REGISTRATION)
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Registration>>> findAll(){
        Flux<Registration> registrationFlux = registrationService.findAll();

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(registrationFlux)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Registration>> save(@Valid @RequestBody Registration registration, final ServerHttpRequest request){
        return registrationService.save(registration)
                .map(e -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                );
    }
}
