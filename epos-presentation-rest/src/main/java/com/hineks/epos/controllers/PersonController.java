package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final PersonRoleService personRoleService;

    @PostMapping(value = "/person/add")
    public ServiceResponse<Person> addPerson(@RequestBody AddOrUpdatePerson person)
    {
        ServiceResponse<Person> response = new ServiceResponse<>();
        if (person.name.length() > 0 && person.password.length() > 0) {
            ServiceResponse<PersonRole> existingRoleResponse = personRoleService.getPersonRoleById(person.roleId);

            if (existingRoleResponse.status) {
                Person newPerson = new Person();
                newPerson.setName(person.name);
                newPerson.setPassword(person.password);
                newPerson.setPersonRole(existingRoleResponse.data);
                newPerson.setImage(person.image);
                newPerson.setCreatedDate(new Date());
                //place.setCreatedBy();
                response = personService.createOrUpdatePerson(newPerson);
            } else {
                response.message = "Rol Id değeri hatalı!";
            }
        }
        else {
            response.message = "Gerekli alanları doldurunuz!";
        }
        return response;
    }

    @PutMapping(value = "/person/update")
    public ServiceResponse<Person> updatePerson(@RequestBody AddOrUpdatePerson person)
    {
        ServiceResponse<Person> response = new ServiceResponse<>();
        if (person.name.length() > 0 && person.password.length() > 0){
            ServiceResponse<PersonRole> existingRoleResponse = personRoleService.getPersonRoleById(person.roleId);
            ServiceResponse<Person> existingPersonResponse = personService.getPersonById(person.id);
            if (existingPersonResponse.status && existingRoleResponse.status) {
                Person existingPerson = existingPersonResponse.data;
                existingPerson.setPersonRole(existingRoleResponse.data);
                existingPerson.setPassword(person.password);
                existingPerson.setName(person.name);
                existingPerson.setImage(person.image);
                response = personService.createOrUpdatePerson(existingPerson);
            } else {
                response.message = "Kullanıcı Id değeri ya da Rol Id değeri hatalı!";
            }
        }
        else {
            response.message = "Gerekli alanları doldurunuz!";
        }
        return response;
    }

    @GetMapping(value = "/person/getPersonById/{id}")
    public ServiceResponse<Person> getPersonById(@PathVariable("id") UUID personId)
    {
        return personService.getPersonById(personId);
    }

    @GetMapping(value = "/person/getPeople")
    public ServiceResponse<List<Person>> getPeople()
    {
        return personService.getPeople();
    }

    @GetMapping(value = "/person/getPeopleByRoleId/{roleId}")
    public ServiceResponse<List<Person>> getPeopleByRoleId(@PathVariable("roleId") UUID roleId)
    {
        return personService.getPeople(roleId);
    }

    @DeleteMapping(value = "/table/deletePersonById/{id}")
    public ServiceResponse<Boolean> deletePersonById(@PathVariable("id") UUID personId)
    {
        return personService.deletePersonById(personId);
    }
}
