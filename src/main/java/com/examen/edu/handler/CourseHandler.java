package com.examen.edu.handler;

import com.examen.edu.models.Course;
import com.examen.edu.service.CourseService;
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
public class CourseHandler {

    @Autowired
    private CourseService courseService;

    @Autowired
    private RequestValidator requestValidator;

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.findAll(), Course.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req){
        String id = req.pathVariable("id");

        return courseService.findById(id)
                .flatMap(course -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(course))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest req){
        Mono<Course> courseMono = req.bodyToMono(Course.class);

        return courseMono
                .flatMap(requestValidator::validate)
                .flatMap(courseService::save)
                .flatMap(course -> ServerResponse
                        .created(URI.create(req.uri().toString().concat("/").concat(course.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(course)));
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        Mono<Course> courseMono = req.bodyToMono(Course.class);
        Mono<Course> monoDB = courseService.findById(id);

        return monoDB.zipWith(courseMono, (db, cm) -> {
                    db.setId(id);
                    db.setName(cm.getName());
                    db.setAcronym(cm.getAcronym());
                    db.setState(cm.getState());

                    return db;
                })
                .flatMap(requestValidator::validate)
                .flatMap(courseService::update)
                .flatMap(course -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(course)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return courseService.findById(id)
                .flatMap(course -> courseService.delete(course.getId())
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
