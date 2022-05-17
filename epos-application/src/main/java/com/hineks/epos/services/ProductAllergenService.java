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
public class ProductAllergenService {
    private final ProductAllergensRepository productAllergensRepository;
    private final ProductDetailService productDetailService;
    private final AllergenService allergenService;

    @Autowired
    public ProductAllergenService(ProductAllergensRepository productAllergensRepository,
                                  @Lazy ProductDetailService productDetailService,
                                  @Lazy AllergenService allergenService)
    {
        this.productAllergensRepository = productAllergensRepository;
        this.productDetailService = productDetailService;
        this.allergenService = allergenService;
    }

    public ServiceResponse<ProductAllergen> createOrUpdateProductAllergen(ProductAllergen productAllergen)
    {
        ServiceResponse<ProductAllergen> response = new ServiceResponse<>();
        try {
            response.data = productAllergensRepository.save(productAllergen);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<ProductAllergen> getProductAllergenById(UUID id)
    {
        ServiceResponse<ProductAllergen> response = new ServiceResponse<>();
        Optional<ProductAllergen> allergen = Optional.empty();
        try {
            allergen = productAllergensRepository.findById(id).filter(p -> !p.getIsDeleted());
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

    public ServiceResponse<List<ProductAllergen>> getProductAllergensByProductDetailId(UUID productDetailId)
    {
        ServiceResponse<List<ProductAllergen>> response = new ServiceResponse<>();
        ServiceResponse<ProductDetail> productDetailResponse = productDetailService.getProductDetailById(productDetailId);
        if (productDetailResponse.status && productDetailResponse.data != null) {
            response.status = true;
            response.message = "Success!";
            response.data = new ArrayList<ProductAllergen>(productDetailResponse.data.getProductAllergens());
        }
        else {
            response.message = productDetailResponse.message;
        }

        return response;
    }

    public ServiceResponse<List<ProductAllergen>> getProductAllergensByAllergenId(UUID allergenId)
    {
        ServiceResponse<List<ProductAllergen>> response = new ServiceResponse<>();
        ServiceResponse<Allergen> allergenResponse = allergenService.getAllergenById(allergenId);
        if (allergenResponse.status && allergenResponse.data != null) {
            response.status = true;
            response.message = "Success!";
            response.data = new ArrayList<ProductAllergen>(allergenResponse.data.getProductAllergens());
        }
        else {
            response.message = allergenResponse.message;
        }

        return response;
    }

    public ServiceResponse<Boolean> deleteProductAllergenById(UUID id)
    {
        ServiceResponse<ProductAllergen> productAllergenResponse = getProductAllergenById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (productAllergenResponse.data != null) {
            try {
                //ProductAllergen isDeleted islemi
                ProductAllergen allergen = productAllergenResponse.data;
                allergen.setIsDeleted(true);
                productAllergensRepository.save(allergen);

                //Response
                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = productAllergenResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<ProductAllergen>> getProductAllergens()
    {
        ServiceResponse<List<ProductAllergen>> response = new ServiceResponse<>();
        try {
            response.data = productAllergensRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
