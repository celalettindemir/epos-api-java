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
public class ProductDetailService {

    private final ProductDetailsRepository productDetailsRepository;
    private final ProductCategoryService productCategoryService;
    private final ProductAllergenService productAllergenService;
    private final ProductDetailDefaultSideService productDetailDefaultSideService;
    private final ProductService productService;

    @Autowired
    public ProductDetailService(ProductDetailsRepository productDetailsRepository,
                                @Lazy ProductCategoryService productCategoryService,
                                @Lazy ProductAllergenService productAllergenService,
                                @Lazy ProductDetailDefaultSideService productDetailDefaultSideService,
                                @Lazy ProductService productService)
    {
        this.productDetailsRepository = productDetailsRepository;
        this.productCategoryService = productCategoryService;
        this.productAllergenService = productAllergenService;
        this.productDetailDefaultSideService = productDetailDefaultSideService;
        this.productService = productService;
    }

    public ServiceResponse<ProductDetail> createOrUpdateProductDetail(ProductDetail productDetail)
    {
        ServiceResponse<ProductDetail> response = new ServiceResponse<>();
        try {
            response.data = productDetailsRepository.save(productDetail);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<ProductDetail> getProductDetailById(UUID id)
    {
        ServiceResponse<ProductDetail> response = new ServiceResponse<>();
        Optional<ProductDetail> productDetail = Optional.empty();
        try {
            productDetail = productDetailsRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (productDetail.isPresent()) {
            response.data = productDetail.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteProductDetailById(UUID id)
    {
        ServiceResponse<ProductDetail> productDetailResponse = getProductDetailById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (productDetailResponse.data != null) {
            try {
                //ProductDetail isDeleted islemi
                ProductDetail productDetail = productDetailResponse.data;
                productDetail.setIsDeleted(true);
                productDetailsRepository.save(productDetail);
                
                //Ürünün defaultsidelarını ve side olduğu ürünlerle bağlantılarının isDeleted işlemi
                for (ProductDetailDefaultSide productDetailDefaultSide :
                        productDetail.getSidesOfProduct()) {
                    productDetailDefaultSideService.deleteProductDetailDefaultSideById(productDetailDefaultSide.getId());
                }
                for (ProductDetailDefaultSide productDetailDefaultSide :
                        productDetail.getProductSides()) {
                    productDetailDefaultSideService.deleteProductDetailDefaultSideById(productDetailDefaultSide.getId());
                }

                //Ürüne baglı alerjenlerin isDeleted islemi
                for (ProductAllergen productAllergen :
                        productDetail.getProductAllergens()) {
                    productAllergenService.deleteProductAllergenById(productAllergen.getId());
                }

                //Ürüne bağlı productların isDeleted islemi
                for (Product product :
                        productDetail.getProducts()) {
                    productService.deleteProductById(product.getId());
                }

                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = productDetailResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<ProductDetail>> getProductDetails()
    {
        ServiceResponse<List<ProductDetail>> response = new ServiceResponse<>();
        try {
            response.data = productDetailsRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<List<ProductDetail>> getProductDetails(UUID categoryId)
    {
        ServiceResponse<List<ProductDetail>> response = new ServiceResponse<>();
        try {
            ServiceResponse<ProductCategory> categoryResponse = productCategoryService.getProductCategoryById(categoryId);
            if (categoryResponse.status) {
                response.data = new ArrayList<ProductDetail>(categoryResponse.data.getProductDetails());
                response.message = "Success!";
                response.status = true;
            }
            else {
                response.message = categoryResponse.message;
            }
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
