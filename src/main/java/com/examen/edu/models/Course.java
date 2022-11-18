package com.examen.edu.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "courses")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Course {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull
    @NotEmpty
    @Size(max = 40)
    private String name;

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 4)
    private String acronym;

    @NotNull
    private Boolean state;
}
