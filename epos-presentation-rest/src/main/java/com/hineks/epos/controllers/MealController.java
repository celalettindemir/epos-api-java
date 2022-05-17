package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @PostMapping(value = "/meal/add")
    public ServiceResponse<Meal> addMeal(@RequestBody String mealName)
    {
        ServiceResponse<Meal> response = new ServiceResponse<>();
        if (mealName.length() > 0) {
            Meal meal = new Meal();
            meal.setMealName(mealName);
            meal.setCreatedDate(new Date());
            //place.setCreatedBy();
            response = mealService.createOrUpdateMeal(meal);
        }
        else {
            response.message = "Öğün adı girmediniz!";
        }
        return response;
    }

    @PutMapping(value = "/meal/update")
    public ServiceResponse<Meal> updateMeal(@RequestBody AddOrUpdateMeal meal)
    {
        ServiceResponse<Meal> response = new ServiceResponse<>();
        if (meal.name.length() > 0){
            ServiceResponse<Meal> getMealResponse = mealService.getMealById(meal.id);
            if (!getMealResponse.status)
                return getMealResponse;

            Meal existing = getMealResponse.data;
            existing.setMealName(meal.name);
            existing.setUpdatedDate(new Date());
            return mealService.createOrUpdateMeal(existing);
        }
        else {
            response.message = "Öğün adı girmediniz!";
        }
        return response;
    }

    @GetMapping(value = "/meal/getMealById/{id}")
    public ServiceResponse<Meal> getMealById(@PathVariable("id") UUID mealId)
    {
        return mealService.getMealById(mealId);
    }

    @GetMapping(value = "/meal/getMeals")
    public ServiceResponse<List<Meal>> getMeals()
    {
        return mealService.getMeals();
    }

    @DeleteMapping(value = "/meal/deleteMealById/{id}")
    public ServiceResponse<Boolean> deleteMealById(@PathVariable("id") UUID mealId)
    {
        return mealService.deleteMealById(mealId);
    }
}
