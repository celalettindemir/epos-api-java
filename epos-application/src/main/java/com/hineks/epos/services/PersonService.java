package com.hineks.epos.services;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.repositories.PeopleRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private final PeopleRepository peopleRepository;
    private final PersonRoleService personRoleService;

    @Autowired
    public PersonService(PeopleRepository peopleRepository, @Lazy PersonRoleService personRoleService) {
        this.peopleRepository = peopleRepository;
        this.personRoleService = personRoleService;
    }

    public ServiceResponse<Person> createOrUpdatePerson(Person person)
    {
        ServiceResponse<Person> response = new ServiceResponse<>();
        try {
            response.data = peopleRepository.save(person);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<Person> getPersonById(UUID id)
    {
        ServiceResponse<Person> response = new ServiceResponse<>();
        Optional<Person> person = Optional.empty();
        try {
            person = peopleRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (person.isPresent()) {
            response.data = person.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deletePersonById(UUID id)
    {
        ServiceResponse<Person> personResponse = getPersonById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (personResponse.data != null) {
            try {
                Person person = personResponse.data;
                person.setIsDeleted(true);
                peopleRepository.save(person);

                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = personResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<Person>> getPeople()
    {
        ServiceResponse<List<Person>> response = new ServiceResponse<>();
        try {
            response.data = peopleRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<List<Person>> getPeople(UUID roleId)
    {
        ServiceResponse<List<Person>> response = new ServiceResponse<>();
        try {
            ServiceResponse<PersonRole> roleResponse = personRoleService.getPersonRoleById(roleId);
            if (roleResponse.status) {
                response.data = new ArrayList<Person>(roleResponse.data.getPeople());
                response.message = "Success!";
                response.status = true;
            }
            else {
                response.message = roleResponse.message;
            }
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
