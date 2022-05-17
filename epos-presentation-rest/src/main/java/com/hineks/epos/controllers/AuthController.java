package com.hineks.epos.controllers;

import com.hineks.epos.definitions.AddOrUpdatePerson;
import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.Person;
import com.hineks.epos.entities.PersonRole;
import com.hineks.epos.services.PersonRoleService;
import com.hineks.epos.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final PersonService personService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final PersonRoleService personRoleService;


    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public void signUp(@RequestBody AddOrUpdatePerson person)
    {
        ServiceResponse<Person> response = new ServiceResponse<>();
        if (person.name.length() > 0 && person.password.length() > 0) {
            ServiceResponse<PersonRole> existingRoleResponse = personRoleService.getPersonRoleById(UUID.fromString("072548b0-9d29-11ea-bb37-0242ac130002"));

            if (existingRoleResponse.status) {
                Person newPerson = new Person();
                newPerson.setName(person.name);
                newPerson.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
                newPerson.setPersonRole(existingRoleResponse.data);
                //newPerson.setImage(person.image);
                newPerson.setCreatedDate(new Date());
                //place.setCreatedBy();
                personService.createOrUpdatePerson(newPerson);
            }
        }
    }

}
