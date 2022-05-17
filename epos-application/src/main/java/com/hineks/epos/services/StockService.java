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
public class StockService {
    private final StockRepository stockRepository;
    private final ProductAdditionStockService productAdditionStockService;

    @Autowired
    public StockService(StockRepository stockRepository,
                        @Lazy ProductAdditionStockService productAdditionStockService)
    {
        this.stockRepository = stockRepository;
        this.productAdditionStockService = productAdditionStockService;
    }

    public ServiceResponse<Stock> createOrUpdateStock(Stock stock)
    {
        ServiceResponse<Stock> response = new ServiceResponse<>();
        try {
            response.data = stockRepository.save(stock);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<Stock> getStockById(UUID id)
    {
        ServiceResponse<Stock> response = new ServiceResponse<>();
        Optional<Stock> stock = Optional.empty();
        try {
            stock = stockRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (stock.isPresent()) {
            response.data = stock.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteStockById(UUID id)
    {
        ServiceResponse<Stock> stockResponse = getStockById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (stockResponse.data != null) {
            try {
                //Stock isDeleted islemi
                Stock stock = stockResponse.data;
                stock.setIsDeleted(true);
                stockRepository.save(stock);

                //Stock tablosuna bağlı product-addition-stockların isDeleted islemi
                for (ProductAdditionStock productAdditionStock :
                        stock.getProductAdditionStocks()) {
                    productAdditionStockService.deleteProductAdditionStockById(productAdditionStock.getId());
                }

                //Stock-transactions kayıtlarda yine de görünmesi gerektiğinden silinmiyor.

                //Response
                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = stockResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<Stock>> getStocks()
    {
        ServiceResponse<List<Stock>> response = new ServiceResponse<>();
        try {
            response.data = stockRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
