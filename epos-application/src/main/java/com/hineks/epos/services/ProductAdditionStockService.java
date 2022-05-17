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
public class ProductAdditionStockService {
    private final ProductAdditionStocksRepository productAdditionStocksRepository;
    private final ProductAdditionService productAdditionService;
    private final StockService stockService;

    @Autowired
    public ProductAdditionStockService(ProductAdditionStocksRepository productAdditionStocksRepository,
                                  @Lazy ProductAdditionService productAdditionService,
                                  @Lazy StockService stockService)
    {
        this.productAdditionStocksRepository = productAdditionStocksRepository;
        this.productAdditionService = productAdditionService;
        this.stockService = stockService;
    }

    public ServiceResponse<ProductAdditionStock> createOrUpdateProductAdditionStock(ProductAdditionStock productAdditionStock)
    {
        ServiceResponse<ProductAdditionStock> response = new ServiceResponse<>();
        try {
            response.data = productAdditionStocksRepository.save(productAdditionStock);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<ProductAdditionStock> getProductAdditionStockById(UUID id)
    {
        ServiceResponse<ProductAdditionStock> response = new ServiceResponse<>();
        Optional<ProductAdditionStock> productAdditionStock = Optional.empty();
        try {
            productAdditionStock = productAdditionStocksRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (productAdditionStock.isPresent()) {
            response.data = productAdditionStock.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<List<ProductAdditionStock>> getProductAdditionStocksByProductAdditionId(UUID productAdditionId)
    {
        ServiceResponse<List<ProductAdditionStock>> response = new ServiceResponse<>();
        ServiceResponse<ProductAddition> productAdditionResponse = productAdditionService.getProductAdditionById(productAdditionId);
        if (productAdditionResponse.status && productAdditionResponse.data != null) {
            response.status = true;
            response.message = "Success!";
            response.data = new ArrayList<ProductAdditionStock>(productAdditionResponse.data.getProductAdditionStocks());
        }
        else {
            response.message = productAdditionResponse.message;
        }

        return response;
    }

    public ServiceResponse<List<ProductAdditionStock>> getProductAdditionStocksByStockId(UUID stockId)
    {
        ServiceResponse<List<ProductAdditionStock>> response = new ServiceResponse<>();
        ServiceResponse<Stock> stockResponse = stockService.getStockById(stockId);
        if (stockResponse.status && stockResponse.data != null) {
            response.status = true;
            response.message = "Success!";
            response.data = new ArrayList<ProductAdditionStock>(stockResponse.data.getProductAdditionStocks());
        }
        else {
            response.message = stockResponse.message;
        }

        return response;
    }

    public ServiceResponse<Boolean> deleteProductAdditionStockById(UUID id)
    {
        ServiceResponse<ProductAdditionStock> productAdditionStockResponse = getProductAdditionStockById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (productAdditionStockResponse.data != null) {
            try {
                //ProductAdditionStock isDeleted islemi
                ProductAdditionStock productAdditionStock = productAdditionStockResponse.data;
                productAdditionStock.setIsDeleted(true);
                productAdditionStocksRepository.save(productAdditionStock);

                //Response
                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = productAdditionStockResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<ProductAdditionStock>> getProductAdditionStocks()
    {
        ServiceResponse<List<ProductAdditionStock>> response = new ServiceResponse<>();
        try {
            response.data = productAdditionStocksRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
