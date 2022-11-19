package com.examen.edu.repository;

import com.examen.edu.models.User;
import reactor.core.publisher.Mono;

public interface UserRepositories extends GenericRepo<User, String> {
    //DerivedQueries
    Mono<User> findOneByUsername(String username);
}
