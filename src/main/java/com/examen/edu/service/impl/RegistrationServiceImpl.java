package com.examen.edu.service.impl;

import com.examen.edu.models.Registration;
import com.examen.edu.repository.GenericRepo;
import com.examen.edu.repository.RegistrationRepositories;
import com.examen.edu.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl extends CrudServiceImpl<Registration, String> implements RegistrationService {

    @Autowired
    private RegistrationRepositories registrationRepositories;

    @Override
    protected GenericRepo<Registration, String> getRepo() {
        return registrationRepositories;
    }
}
