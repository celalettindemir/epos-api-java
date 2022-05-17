package com.hineks.epos.services;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.*;
import com.hineks.epos.repositories.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductDetailDefaultSideService {

    private final ProductDetailDefaultSidesRepository productDetailDefaultSidesRepository;
    private final ProductDetailService productDetailService;

    @Autowired
    public ProductDetailDefaultSideService(ProductDetailDefaultSidesRepository productDetailDefaultSidesRepository,
                                           @Lazy ProductDetailService productDetailService)
    {
        this.productDetailDefaultSidesRepository = productDetailDefaultSidesRepository;
        this.productDetailService = productDetailService;
    }

    public ServiceResponse<ProductDetailDefaultSide> createOrUpdateProductDetailDefaultSide(ProductDetailDefaultSide productDetailDefaultSide)
    {
        ServiceResponse<ProductDetailDefaultSide> response = new ServiceResponse<>();
        try {
            response.data = productDetailDefaultSidesRepository.save(productDetailDefaultSide);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<ProductDetailDefaultSide> getProductDetailDefaultSideById(UUID id)
    {
        ServiceResponse<ProductDetailDefaultSide> response = new ServiceResponse<>();
        Optional<ProductDetailDefaultSide> productDetailDefaultSide = Optional.empty();
        try {
            productDetailDefaultSide = productDetailDefaultSidesRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (productDetailDefaultSide.isPresent()) {
            response.data = productDetailDefaultSide.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<List<ProductDetailDefaultSide>> getProductDetailDefaultSidesByProductDetailId(UUID productDetailId)
    {
        ServiceResponse<List<ProductDetailDefaultSide>> response = new ServiceResponse<>();
        ServiceResponse<ProductDetail> productDetailResponse = productDetailService.getProductDetailById(productDetailId);
        if (productDetailResponse.status && productDetailResponse.data != null) {
            response.status = true;
            response.message = "Success!";
            response.data = new ArrayList<ProductDetailDefaultSide>(productDetailResponse.data.getSidesOfProduct());
        }
        else {
            response.message = productDetailResponse.message;
        }

        return response;
    }

    public ServiceResponse<List<ProductDetailDefaultSide>> getProductDetailDefaultSidesBySideProductDetailId(UUID sideProductDetailId)
    {
        ServiceResponse<List<ProductDetailDefaultSide>> response = new ServiceResponse<>();
        ServiceResponse<ProductDetail> productDetailResponse = productDetailService.getProductDetailById(sideProductDetailId);
        if (productDetailResponse.status && productDetailResponse.data != null) {
            response.status = true;
            response.message = "Success!";
            response.data = new ArrayList<ProductDetailDefaultSide>(productDetailResponse.data.getProductSides());
        }
        else {
            response.message = productDetailResponse.message;
        }

        return response;
    }

    public ServiceResponse<Boolean> deleteProductDetailDefaultSideById(UUID id)
    {
        ServiceResponse<ProductDetailDefaultSide> productDetailDefaultSideResponse = getProductDetailDefaultSideById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (productDetailDefaultSideResponse.data != null) {
            try {
                //ProductDetailDefaultSide isDeleted islemi
                ProductDetailDefaultSide productDetailDefaultSide = productDetailDefaultSideResponse.data;
                productDetailDefaultSide.setIsDeleted(true);
                productDetailDefaultSidesRepository.save(productDetailDefaultSide);

                //Response
                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = productDetailDefaultSideResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<ProductDetailDefaultSide>> getProductDetailDefaultSides()
    {
        ServiceResponse<List<ProductDetailDefaultSide>> response = new ServiceResponse<>();
        try {
            response.data = productDetailDefaultSidesRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
