package com.examen.edu.repository;

import com.examen.edu.models.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepositories extends GenericRepo<Student, String>{
}
