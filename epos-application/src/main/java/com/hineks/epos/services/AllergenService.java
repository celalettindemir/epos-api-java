package com.hineks.epos.services;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.repositories.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AllergenService {
    private final AllergensRepository allergensRepository;

    @Autowired
    public AllergenService(AllergensRepository allergensRepository)
    {
        this.allergensRepository = allergensRepository;
    }

    public ServiceResponse<Allergen> createOrUpdateAllergen(Allergen allergen)
    {
        ServiceResponse<Allergen> response = new ServiceResponse<>();
        try {
            response.data = allergensRepository.save(allergen);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<Allergen> getAllergenById(UUID id)
    {
        ServiceResponse<Allergen> response = new ServiceResponse<>();
        Optional<Allergen> allergen = Optional.empty();
        try {
            allergen = allergensRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (allergen.isPresent()) {
            response.data = allergen.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteAllergenById(UUID id)
    {
        ServiceResponse<Allergen> allergenResponse = getAllergenById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (allergenResponse.data != null) {
            try {
                //Allergen isDeleted islemi
                Allergen allergen = allergenResponse.data;
                allergen.setIsDeleted(true);
                allergensRepository.save(allergen);

                //Meal altındaki ürünlerin isDeleted islemi
                /*for (Product product :
                        meal.getProducts()) {
                    productService.deleteProductById(product.getId());
                }*/

                //Response
                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = allergenResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<Allergen>> getAllergens()
    {
        ServiceResponse<List<Allergen>> response = new ServiceResponse<>();
        try {
            response.data = allergensRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
