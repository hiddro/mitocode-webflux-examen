package com.examen.edu.service.impl;

import com.examen.edu.models.Course;
import com.examen.edu.repository.CourseRepositories;
import com.examen.edu.repository.GenericRepo;
import com.examen.edu.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends CrudServiceImpl<Course, String> implements CourseService {

    @Autowired
    private CourseRepositories courseRepositories;

    @Override
    protected GenericRepo<Course, String> getRepo() {
        return courseRepositories;
    }
}
