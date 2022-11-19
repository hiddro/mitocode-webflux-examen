package com.examen.edu.service.impl;

import com.examen.edu.models.User;
import com.examen.edu.repository.GenericRepo;
import com.examen.edu.repository.RoleRepositories;
import com.examen.edu.repository.UserRepositories;
import com.examen.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends CrudServiceImpl<User, String> implements UserService {

    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    private RoleRepositories roleRepositories;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected GenericRepo<User, String> getRepo() {
        return userRepositories;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepositories.save(user);
    }

    @Override
    public Mono<com.examen.edu.security.models.User> searchByUser(String username) {
        Mono<User> userMono = userRepositories.findOneByUsername(username);
        List<String> roles = new ArrayList<>();

        return userMono.flatMap(u -> {
                    return Flux.fromIterable(u.getRoles())
                            .flatMap(rol -> {
                                return roleRepositories.findById(rol.getId())
                                        .map(r -> {
                                            roles.add(r.getName());
                                            return r;
                                        });
                            })
                            .collectList()
                            .flatMap(list -> {
                                u.setRoles(list);
                                return Mono.just(u);
                            });
                })
                .flatMap(u -> Mono.just(new com.examen.edu.security.models.User(u.getUsername(), u.getPassword(), u.getStatus(), roles)));
    }
}
