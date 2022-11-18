package com.examen.edu.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
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
@Document(collection = "students")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull
    @NotEmpty
    @Size(max = 40)
    private String names;

    @NotNull
    @NotEmpty
    @Size(max = 40)
    private String surnames;

    @NotNull
    @Size(min = 8, max = 8)
    private String dni;

    @NotNull
    private Integer age;
}
