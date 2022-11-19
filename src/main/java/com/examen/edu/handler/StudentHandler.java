package com.examen.edu.handler;

import com.examen.edu.models.Student;
import com.examen.edu.service.StudentService;
import com.examen.edu.utils.Constants;
import com.examen.edu.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Comparator;

@Component
public class StudentHandler {

    @Autowired
    private StudentService studentService;

    @Autowired
    private RequestValidator requestValidator;

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService.findAll(), Student.class);
    }

    public Mono<ServerResponse> getOrder(ServerRequest req){
        String key = req.pathVariable("key");
        Flux<Student> studentFlux = studentService.findAll()
                .sort(key.equalsIgnoreCase(Constants.STRING_ASCENDING) ? Comparator.comparing(Student::getAge) : Comparator.comparing(Student::getAge).reversed());

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentFlux, Student.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req){
        String id = req.pathVariable("id");

        return studentService.findById(id)
                .flatMap(student -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(student))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest req){
        Mono<Student> studentMono = req.bodyToMono(Student.class);

        return studentMono
                .flatMap(requestValidator::validate)
                .flatMap(studentService::save)
                .flatMap(student -> ServerResponse
                        .created(URI.create(req.uri().toString().concat("/").concat(student.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(student)));
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        Mono<Student> studentMono = req.bodyToMono(Student.class);
        Mono<Student> monoDB = studentService.findById(id);

        return monoDB.zipWith(studentMono, (db, sm) -> {
                    db.setId(id);
                    db.setNames(sm.getNames());
                    db.setSurnames(sm.getSurnames());
                    db.setDni(sm.getDni());
                    db.setAge(sm.getAge());

                    return db;
                })
                .flatMap(requestValidator::validate)
                .flatMap(studentService::update)
                .flatMap(student -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(student)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return studentService.findById(id)
                .flatMap(student -> studentService.delete(student.getId())
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
