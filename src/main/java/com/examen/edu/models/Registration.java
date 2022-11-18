package com.examen.edu.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "invoices")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Registration {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull
    private Student student;

    private List<Course> courses;

    @NotNull
    private LocalDate registerDate;

    @NotNull
    private Boolean state;
}
