package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AllergenController {
    private final AllergenService allergenService;

    @PostMapping(value = "/allergen/add")
    public ServiceResponse<Allergen> addAllergen(@RequestBody AddOrUpdateAllergen allergen)
    {
        ServiceResponse<Allergen> response = new ServiceResponse<>();
        if (allergen.name.length() > 0) {
            Allergen newAllergen = new Allergen();
            newAllergen.setAllergenName(allergen.name);
            newAllergen.setIcon(allergen.icon);
            newAllergen.setCreatedDate(new Date());
            //newAllergen.setCreatedBy();
            response = allergenService.createOrUpdateAllergen(newAllergen);
        }
        else {
            response.message = "Alerjen adı girmediniz!";
        }
        return response;
    }

    @PutMapping(value = "/allergen/update")
    public ServiceResponse<Allergen> updateAllergen(@RequestBody AddOrUpdateAllergen allergen)
    {
        ServiceResponse<Allergen> response = new ServiceResponse<>();
        if (allergen.name.length() > 0){
            ServiceResponse<Allergen> getAllergenResponse = allergenService.getAllergenById(allergen.id);
            if (!getAllergenResponse.status)
                return getAllergenResponse;

            Allergen existing = getAllergenResponse.data;
            existing.setAllergenName(allergen.name);
            existing.setIcon(allergen.icon);
            existing.setUpdatedDate(new Date());
            return allergenService.createOrUpdateAllergen(existing);
        }
        else {
            response.message = "Alerjen adı girmediniz!";
        }
        return response;
    }

    @GetMapping(value = "/allergen/getAllergenById/{id}")
    public ServiceResponse<Allergen> getAllergenById(@PathVariable("id") UUID allergenId)
    {
        return allergenService.getAllergenById(allergenId);
    }

    @GetMapping(value = "/allergen/getAllergens")
    public ServiceResponse<List<Allergen>> getAllergens()
    {
        return allergenService.getAllergens();
    }

    @DeleteMapping(value = "/allergen/deleteAllergenById/{id}")
    public ServiceResponse<Boolean> deleteAllergenById(@PathVariable("id") UUID allergenId)
    {
        return allergenService.deleteAllergenById(allergenId);
    }
}
