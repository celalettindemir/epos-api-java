package com.hineks.epos.controllers;

import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import com.hineks.epos.services.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final PlacesService placesService;
    private final TableService tableService;
    private final PersonRoleService personRoleService;

    @RequestMapping(value = "/getPlaceList",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Table> findAll()
    {
        List<Table> placesList = null;//placesService.findAll();
        return placesList;
    }

    @RequestMapping(value = "/getTables",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Table> getAllTables()
    {
        List<Table> tableListList = null;//tableService.findAll();
        return tableListList;
    }

    /*@RequestMapping(value = "/getRoles",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonRole> getRoles()
    {
        List<PersonRole> roles = personRoleService.findAll();
        return roles;
    }

    @RequestMapping(value = "/createPersonRole",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonRole createPersonRole()
    {
        PersonRole role = new PersonRole();
        role.setName("Deneme");
        role.setCreatedDate(new Date());
        personRoleService.CreateRole(role);

        return role;
    }*/
}
