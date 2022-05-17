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
public class ProductAdditionService {
    private final ProductAdditionsRepository productAdditionsRepository;
    private final ProductService productService;
    private final ProductAdditionStockService productAdditionStockService;

    @Autowired
    public ProductAdditionService(ProductAdditionsRepository productAdditionsRepository,
                                  @Lazy ProductService productService,
                                  @Lazy ProductAdditionStockService productAdditionStockService)
    {
        this.productAdditionsRepository = productAdditionsRepository;
        this.productService = productService;
        this.productAdditionStockService = productAdditionStockService;
    }

    public ServiceResponse<ProductAddition> createOrUpdateProductAddition(ProductAddition productAddition)
    {
        ServiceResponse<ProductAddition> response = new ServiceResponse<>();
        try {
            response.data = productAdditionsRepository.save(productAddition);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<ProductAddition> getProductAdditionById(UUID id)
    {
        ServiceResponse<ProductAddition> response = new ServiceResponse<>();
        Optional<ProductAddition> addition = Optional.empty();
        try {
            addition = productAdditionsRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (addition.isPresent()) {
            response.data = addition.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<List<ProductAddition>> getProductAdditionsByProductId(UUID productId)
    {
        ServiceResponse<List<ProductAddition>> response = new ServiceResponse<>();
        ServiceResponse<Product> productResponse = productService.getProductById(productId);
        if (productResponse.status && productResponse.data != null) {
            response.status = true;
            response.message = "Success!";
            response.data = new ArrayList<ProductAddition>(productResponse.data.getProductAdditions());
        }
        else {
            response.message = productResponse.message;
        }

        return response;
    }

    public ServiceResponse<Boolean> deleteProductAdditionById(UUID id)
    {
        ServiceResponse<ProductAddition> productAdditionResponse = getProductAdditionById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (productAdditionResponse.data != null) {
            try {
                //ProductAddition isDeleted islemi
                ProductAddition addition = productAdditionResponse.data;
                addition.setIsDeleted(true);
                productAdditionsRepository.save(addition);

                //ProductAddition'a bağlı productAddition-stockların silinmesi
                for (ProductAdditionStock pas :
                        addition.getProductAdditionStocks()) {
                    productAdditionStockService.deleteProductAdditionStockById(pas.getId());
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
            response.message = productAdditionResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<ProductAddition>> getProductAdditions()
    {
        ServiceResponse<List<ProductAddition>> response = new ServiceResponse<>();
        try {
            response.data = productAdditionsRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
