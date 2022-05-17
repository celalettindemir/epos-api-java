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
public class ProductService {
    private final ProductsRepository productsRepository;
    private final ProductDetailService productDetailService;
    private final ProductAdditionService productAdditionService;

    @Autowired
    public ProductService(ProductsRepository productsRepository,
                          @Lazy ProductDetailService productDetailService,
                          @Lazy ProductAdditionService productAdditionService)
    {
        this.productsRepository = productsRepository;
        this.productDetailService = productDetailService;
        this.productAdditionService = productAdditionService;
    }

    public ServiceResponse<Product> createOrUpdateProduct(Product product)
    {
        ServiceResponse<Product> response = new ServiceResponse<>();
        try {
            response.data = productsRepository.save(product);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<Product> getProductById(UUID id)
    {
        ServiceResponse<Product> response = new ServiceResponse<>();
        Optional<Product> product = Optional.empty();
        try {
            product = productsRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (product.isPresent()) {
            response.data = product.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteProductById(UUID id)
    {
        ServiceResponse<Product> productResponse = getProductById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (productResponse.data != null) {
            try {
                //Product isDeleted işlemi
                Product product = productResponse.data;
                product.setIsDeleted(true);
                productsRepository.save(product);

                //Producta bağlı product-additionların isDeleted işlemi
                for (ProductAddition productAddition :
                        product.getProductAdditions()) {
                    productAdditionService.deleteProductAdditionById(productAddition.getId());
                }

                //Producta bağlı ticket-items var fakat silinmeyecekler.

                //Response
                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = productResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<Product>> getProducts()
    {
        ServiceResponse<List<Product>> response = new ServiceResponse<>();
        try {
            response.data = productsRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<List<Product>> getProducts(UUID productDetailId)
    {
        ServiceResponse<List<Product>> response = new ServiceResponse<>();
        try {
            ServiceResponse<ProductDetail> detailResponse = productDetailService.getProductDetailById(productDetailId);
            if (detailResponse.status) {
                response.data = new ArrayList<Product>(detailResponse.data.getProducts());
                response.message = "Success!";
                response.status = true;
            }
            else {
                response.message = detailResponse.message;
            }
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
