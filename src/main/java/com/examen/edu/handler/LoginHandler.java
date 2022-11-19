package com.examen.edu.handler;

import com.examen.edu.security.exceptions.ErrorLogin;
import com.examen.edu.security.models.AuthRequest;
import com.examen.edu.security.models.AuthResponse;
import com.examen.edu.security.utils.JWTUtil;
import com.examen.edu.service.UserService;
import com.examen.edu.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

@Component
public class LoginHandler {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    public Mono<ServerResponse> login(ServerRequest req){
        Mono<AuthRequest> authRequestMono = req.bodyToMono(AuthRequest.class);

        return authRequestMono
                .flatMap(auth -> userService.searchByUser(auth.getUsername())
                        .map(userDetails -> {
                            if(BCrypt.checkpw(auth.getPassword(), userDetails.getPassword())){
                                String token = jwtUtil.generateToken(userDetails);
                                Date expiration = jwtUtil.getExpirationDateFromToken(token);
                                return new AuthResponse(token, expiration);
                            }else{
                                return new ErrorLogin("Bad Credentials", new Date());
                            }
                        })
                        .switchIfEmpty(Mono.just(new ErrorLogin("Bad Credentials", new Date())))
                )
                .flatMap(log -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(log)));
    }
}
