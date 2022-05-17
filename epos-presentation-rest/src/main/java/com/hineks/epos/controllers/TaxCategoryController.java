package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class TaxCategoryController {
    private final TaxCategoryService taxCategoryService;

    @PostMapping(value = "/tax-category/add")
    public ServiceResponse<TaxCategory> addTaxCategory(@RequestBody AddOrUpdateTaxCategory taxCategory)
    {
        ServiceResponse<TaxCategory> response = new ServiceResponse<>();
        if (taxCategory.name.length() > 0) {
            TaxCategory newTaxCategory = new TaxCategory();
            newTaxCategory.setName(taxCategory.name);
            newTaxCategory.setRate(taxCategory.rate);
            newTaxCategory.setCreatedDate(new Date());
            //place.setCreatedBy();
            response = taxCategoryService.createOrUpdateTaxCategory(newTaxCategory);
        }
        else {
            response.message = "Tax adı girmediniz!";
        }
        return response;
    }

    @PutMapping(value = "/tax-category/update")
    public ServiceResponse<TaxCategory> updateTaxCategory(@RequestBody AddOrUpdateTaxCategory taxCategory)
    {
        ServiceResponse<TaxCategory> response = new ServiceResponse<>();
        if (taxCategory.name.length() > 0){
            ServiceResponse<TaxCategory> getTaxCategoryResponse = taxCategoryService.getTaxCategoryById(taxCategory.id);
            if (!getTaxCategoryResponse.status)
                return getTaxCategoryResponse;

            TaxCategory existing = getTaxCategoryResponse.data;
            existing.setName(taxCategory.name);
            existing.setRate(taxCategory.rate);
            existing.setUpdatedDate(new Date());
            return taxCategoryService.createOrUpdateTaxCategory(existing);
        }
        else {
            response.message = "Tax adı girmediniz!";
        }
        return response;
    }

    @GetMapping(value = "/tax-category/getTaxCategoryById/{id}")
    public ServiceResponse<TaxCategory> getTaxCategoryById(@PathVariable("id") UUID taxCategoryId)
    {
        return taxCategoryService.getTaxCategoryById(taxCategoryId);
    }

    @GetMapping(value = "/tax-category/getTaxCategories")
    public ServiceResponse<List<TaxCategory>> getTaxCategories()
    {
        return taxCategoryService.getTaxCategories();
    }

    @DeleteMapping(value = "/tax-category/deleteTaxCategoryById/{id}")
    public ServiceResponse<Boolean> deleteTaxCategoryById(@PathVariable("id") UUID taxCategoryId)
    {
        return taxCategoryService.deleteTaxCategoryById(taxCategoryId);
    }
}
