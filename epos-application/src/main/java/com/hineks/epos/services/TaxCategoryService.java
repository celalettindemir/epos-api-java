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
public class TaxCategoryService {
    private final TaxCategoriesRepository taxCategoriesRepository;
    private final ProductDetailService productDetailService;
    @Autowired
    public TaxCategoryService(TaxCategoriesRepository taxCategoriesRepository,
                        @Lazy ProductDetailService productDetailService)
    {
        this.taxCategoriesRepository = taxCategoriesRepository;
        this.productDetailService = productDetailService;
    }

    public ServiceResponse<TaxCategory> createOrUpdateTaxCategory(TaxCategory taxCategory)
    {
        ServiceResponse<TaxCategory> response = new ServiceResponse<>();
        try {
            response.data = taxCategoriesRepository.save(taxCategory);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<TaxCategory> getTaxCategoryById(UUID id)
    {
        ServiceResponse<TaxCategory> response = new ServiceResponse<>();
        Optional<TaxCategory> taxCategory = Optional.empty();
        try {
            taxCategory = taxCategoriesRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (taxCategory.isPresent()) {
            response.data = taxCategory.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteTaxCategoryById(UUID id)
    {
        ServiceResponse<TaxCategory> taxCategoryResponse = getTaxCategoryById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (taxCategoryResponse.data != null) {
            try {
                //Stock isDeleted islemi
                TaxCategory taxCategory = taxCategoryResponse.data;
                taxCategory.setIsDeleted(true);
                taxCategoriesRepository.save(taxCategory);

                //TaxCategory altındaki productdetails isDeleted islemi
                for (ProductDetail productDetail :
                        taxCategory.getProductDetails()) {
                    productDetailService.deleteProductDetailById(productDetail.getId());
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
            response.message = taxCategoryResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<TaxCategory>> getTaxCategories()
    {
        ServiceResponse<List<TaxCategory>> response = new ServiceResponse<>();
        try {
            response.data = taxCategoriesRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
