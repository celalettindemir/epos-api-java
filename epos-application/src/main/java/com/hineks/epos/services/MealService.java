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
public class MealService {
    private final MealsRepository mealsRepository;
    private final ProductService productService;

    @Autowired
    public MealService(MealsRepository mealsRepository, @Lazy ProductService productService)
    {
        this.mealsRepository = mealsRepository;
        this.productService = productService;
    }

    public ServiceResponse<Meal> createOrUpdateMeal(Meal meal)
    {
        ServiceResponse<Meal> response = new ServiceResponse<>();
        try {
            response.data = mealsRepository.save(meal);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<Meal> getMealById(UUID id)
    {
        ServiceResponse<Meal> response = new ServiceResponse<>();
        Optional<Meal> meal = Optional.empty();
        try {
            meal = mealsRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (meal.isPresent()) {
            response.data = meal.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteMealById(UUID id)
    {
        ServiceResponse<Meal> mealResponse = getMealById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (mealResponse.data != null) {
            try {
                //Meal isDeleted islemi
                Meal meal = mealResponse.data;
                meal.setIsDeleted(true);
                mealsRepository.save(meal);

                //Meal altındaki ürünlerin isDeleted islemi
                for (Product product :
                        meal.getProducts()) {
                    productService.deleteProductById(product.getId());
                }

                //Response
                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = mealResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<Meal>> getMeals()
    {
        ServiceResponse<List<Meal>> response = new ServiceResponse<>();
        try {
            response.data = mealsRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
