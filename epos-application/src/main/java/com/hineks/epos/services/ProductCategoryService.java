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
public class ProductCategoryService {

    private final ProductCategoriesRepository productCategoriesRepository;
    private final ProductDetailService productDetailService;

    @Autowired
    public ProductCategoryService(ProductCategoriesRepository productCategoriesRepository, @Lazy ProductDetailService productDetailService)
    {
        this.productCategoriesRepository = productCategoriesRepository;
        this.productDetailService = productDetailService;
    }

    public ServiceResponse<ProductCategory> createOrUpdateProductCategory(ProductCategory productCategory)
    {
        ServiceResponse<ProductCategory> response = new ServiceResponse<ProductCategory>();
        try {
            response.data = productCategoriesRepository.save(productCategory);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<ProductCategory> getProductCategoryById(UUID id)
    {
        ServiceResponse<ProductCategory> response = new ServiceResponse<>();
        Optional<ProductCategory> productCategory = Optional.empty();
        try {
            productCategory = productCategoriesRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (productCategory.isPresent()) {
            response.data = productCategory.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<List<ProductCategory>> getSubProductCategories(UUID id)
    {
        ServiceResponse<List<ProductCategory>> response = new ServiceResponse<>();
        if (id != null) {
            ServiceResponse<ProductCategory> categoryResponse = getProductCategoryById(id);
            if (categoryResponse.status) {
                response.data = new ArrayList<>(categoryResponse.data.getSubCategories());
                response.status = true;
                response.message = "Success!";
            } else {
                response.message = "Kayıt bulunamadı.";
            }
        }
        else {
            ServiceResponse<List<ProductCategory>> categoryResponse = getProductCategories();
            if (categoryResponse.status) {
                List<ProductCategory> categories = categoryResponse.data;
                categories.removeIf(p -> p.getParent() != null);

                response.data = categories;
                response.status = true;
                response.message = "Success!";
            }
            else {
                response.message = categoryResponse.message;
            }
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteProductCategoryById(UUID id)
    {
        ServiceResponse<ProductCategory> productCategoryResponse = getProductCategoryById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (productCategoryResponse.data != null) {
            try {
                //ProductCategory isDeleted islemi
                ProductCategory category = productCategoryResponse.data;
                category.setIsDeleted(true);
                productCategoriesRepository.save(category);

                //SubCategories isDeleted islemi
                for (ProductCategory subCategory :
                        category.getSubCategories()) {
                    deleteProductCategoryById(subCategory.getId());
                }

                //ProductCategory altındaki urunlerin isDeleted islemi
                for (ProductDetail productDetail :
                        category.getProductDetails()) {
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
            response.message = productCategoryResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<ProductCategory>> getProductCategories()
    {
        ServiceResponse<List<ProductCategory>> response = new ServiceResponse<>();
        try {
            response.data = productCategoriesRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
