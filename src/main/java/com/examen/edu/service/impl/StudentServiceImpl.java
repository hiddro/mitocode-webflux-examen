package com.examen.edu.service.impl;

import com.examen.edu.models.Student;
import com.examen.edu.repository.GenericRepo;
import com.examen.edu.repository.StudentRepositories;
import com.examen.edu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends CrudServiceImpl<Student, String> implements StudentService {

    @Autowired
    private StudentRepositories studentRepositories;

    @Override
    protected GenericRepo<Student, String> getRepo() {
        return studentRepositories;
    }
}
