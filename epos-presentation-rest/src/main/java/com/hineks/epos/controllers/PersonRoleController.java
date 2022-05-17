package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PersonRoleController {
    private final PersonRoleService personRoleService;

    @PostMapping(value = "/personRole/add")
    public ServiceResponse<PersonRole> addPersonRole(@RequestBody String roleName)
    {
        ServiceResponse<PersonRole> response = new ServiceResponse<>();
        if (roleName.length() > 0) {
            PersonRole personRole = new PersonRole();
            personRole.setName(roleName);
            personRole.setCreatedDate(new Date());
            //place.setCreatedBy();
            response = personRoleService.createOrUpdatePersonRole(personRole);
        }
        else {
            response.message = "Rol adı girmediniz!";
        }
        return response;
    }

    @PutMapping(value = "/personRole/update")
    public ServiceResponse<PersonRole> updatePersonRole(@RequestBody UpdatePersonRole personRole)
    {
        ServiceResponse<PersonRole> response = new ServiceResponse<>();
        if (personRole.roleName.length() > 0){
            ServiceResponse<PersonRole> getPersonRoleResponse = personRoleService.getPersonRoleById(personRole.id);
            if (!getPersonRoleResponse.status)
                return getPersonRoleResponse;

            PersonRole existing = getPersonRoleResponse.data;
            existing.setName(personRole.roleName);
            existing.setUpdatedDate(new Date());
            return personRoleService.createOrUpdatePersonRole(existing);
        }
        else {
            response.message = "Rol adı girmediniz!";
        }
        return response;
    }

    @GetMapping(value = "/personRole/getPersonRoleById/{id}")
    public ServiceResponse<PersonRole> getPersonRoleById(@PathVariable("id") UUID personRoleId)
    {
        return personRoleService.getPersonRoleById(personRoleId);
    }

    @GetMapping(value = "/personRole/getPersonRoles")
    public ServiceResponse<List<PersonRole>> getPersonRoles()
    {
        return personRoleService.getPersonRoles();
    }

    @DeleteMapping(value = "/personRole/deletePersonRoleById/{id}")
    public ServiceResponse<Boolean> deletePersonRoleById(@PathVariable("id") UUID personRoleId)
    {
        return personRoleService.deletePersonRoleById(personRoleId);
    }
}
