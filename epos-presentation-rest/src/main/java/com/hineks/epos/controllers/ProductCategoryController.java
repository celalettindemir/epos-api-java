package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @PostMapping(value = "/productCategory/add")
    public ServiceResponse<ProductCategory> addProductCategory(@RequestBody AddOrUpdateProductCategory productCategory)
    {
        ServiceResponse<ProductCategory> response = new ServiceResponse<>();
        if (productCategory.name.length() > 0) {
            ServiceResponse<ProductCategory> superCategoryResponse = productCategoryService.getProductCategoryById(productCategory.parentId);

            ProductCategory newProductCategory = new ProductCategory();
            newProductCategory.setCategoryName(productCategory.name);
            newProductCategory.setImage(productCategory.image);
            newProductCategory.setTextTip(productCategory.textTip);
            newProductCategory.setCreatedDate(new Date());
            if (superCategoryResponse.status && superCategoryResponse.data != null) {
                newProductCategory.setParent(superCategoryResponse.data);
            }
            //place.setCreatedBy();
            response = productCategoryService.createOrUpdateProductCategory(newProductCategory);
        }
        else {
            response.message = "Kategori adı girmediniz!";
        }
        return response;
    }

    @PutMapping(value = "/productCategory/update")
    public ServiceResponse<ProductCategory> updateProductCategory(@RequestBody AddOrUpdateProductCategory productCategory)
    {
        ServiceResponse<ProductCategory> response = new ServiceResponse<>();
        if (productCategory.name.length() > 0){
            ServiceResponse<ProductCategory> getProductCategoryResponse = productCategoryService.getProductCategoryById(productCategory.id);
            if (!getProductCategoryResponse.status)
                return getProductCategoryResponse;

            ServiceResponse<ProductCategory> superCategoryResponse = productCategoryService.getProductCategoryById(productCategory.parentId);

            ProductCategory existingProductCategory = getProductCategoryResponse.data;
            existingProductCategory.setCategoryName(productCategory.name);
            existingProductCategory.setImage(productCategory.image);
            existingProductCategory.setTextTip(productCategory.textTip);
            existingProductCategory.setCreatedDate(new Date());
            if (superCategoryResponse.status && superCategoryResponse.data != null) {
                existingProductCategory.setParent(superCategoryResponse.data);
            }
            existingProductCategory.setUpdatedDate(new Date());
            //place.setCreatedBy();
            response = productCategoryService.createOrUpdateProductCategory(existingProductCategory);
        }
        else {
            response.message = "Kategori adı girmediniz!";
        }
        return response;
    }

    @GetMapping(value = "/productCategory/getProductCategoryById/{id}")
    public ServiceResponse<ProductCategory> getProductCategoryById(@PathVariable("id") UUID productCategoryId)
    {
        return productCategoryService.getProductCategoryById(productCategoryId);
    }

    @GetMapping(value = "/productCategory/getProductCategories")
    @ResponseBody
    public ServiceResponse<List<ProductCategory>> getProductCategories(@RequestParam(required = false) UUID id)
    {
        if (id != null) {
            return productCategoryService.getSubProductCategories(id);
        }
        else {
            return productCategoryService.getSubProductCategories(null);
        }
    }

    @DeleteMapping(value = "/productCategory/deleteProductCategoryById/{id}")
    public ServiceResponse<Boolean> deleteProductCategoryById(@PathVariable("id") UUID productCategoryId)
    {
        return productCategoryService.deleteProductCategoryById(productCategoryId);
    }
}
