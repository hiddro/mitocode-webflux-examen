package com.examen.edu.service;

import com.examen.edu.models.User;
import reactor.core.publisher.Mono;

public interface UserService extends CrudService<User, String> {
    Mono<User> saveHash(User user);

    Mono<com.examen.edu.security.models.User> searchByUser(String username);
}
