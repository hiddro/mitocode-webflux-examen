package com.examen.edu.controller;

import com.examen.edu.models.Course;
import com.examen.edu.service.CourseService;
import com.examen.edu.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(Constants.URL_COURSE)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Course>>> findAll(){
        Flux<Course> courseFlux = courseService.findAll();

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseFlux)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Course>> findById(@PathVariable("id") String id){
        return courseService.findById(id)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Course>> save(@Valid @RequestBody Course course, final ServerHttpRequest request){
        return courseService.save(course)
                .map(e -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Course>> update(@PathVariable("id") String id, @Valid @RequestBody Course course){
        course.setId(id);

        Mono<Course> courseMono = Mono.just(course);
        Mono<Course> courseDB = courseService.findById(id);

        return courseDB.zipWith(courseMono, (db, cm) -> {
                    db.setId(id);
                    db.setName(cm.getName());
                    db.setAcronym(cm.getAcronym());
                    db.setState(cm.getState());

                    return db;
                })
                .flatMap(courseService::update)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return courseService.findById(id)
                .flatMap(e -> courseService.delete(e.getId())
                        .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
