package com.examen.edu.controller;

import com.examen.edu.models.Student;
import com.examen.edu.models.Variables.StudentOrder;
import com.examen.edu.service.StudentService;
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
import java.util.Comparator;

@RestController
@RequestMapping(Constants.URL_STUDENT)
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Student>>> findAll(){
        Flux<Student> studentFlux = studentService.findAll();

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentFlux)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{key}") // -> A o D
    public Mono<ResponseEntity<Flux<Student>>> getOrder(@PathVariable(value = "key", required = true) StudentOrder key){
        Flux<Student> studentFlux = studentService.findAll()
                .sort(Constants.STRING_ASCENDING.equalsIgnoreCase(key.toString()) ? Comparator.comparing(Student::getAge) : Comparator.comparing(Student::getAge).reversed());

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentFlux)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Student>> findById(@PathVariable("id") String id){
        return studentService.findById(id)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Student>> save(@Valid @RequestBody Student student, final ServerHttpRequest request){
        return studentService.save(student)
                .map(e -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Student>> update(@PathVariable("id") String id, @Valid @RequestBody Student student){
        student.setId(id);

        Mono<Student> studentMono = Mono.just(student);
        Mono<Student> studentDB = studentService.findById(id);

        return studentDB.zipWith(studentMono, (db, sm) -> {
                    db.setId(id);
                    db.setNames(sm.getNames());
                    db.setSurnames(sm.getSurnames());
                    db.setDni(sm.getDni());
                    db.setAge(sm.getAge());

                    return db;
                })
                .flatMap(studentService::update)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return studentService.findById(id)
                .flatMap(e -> studentService.delete(e.getId())
                        .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
