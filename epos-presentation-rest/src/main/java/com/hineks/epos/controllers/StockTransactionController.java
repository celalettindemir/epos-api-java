package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class StockTransactionController {
    private final StockTransactionService stockTransactionService;
    private final StockService stockService;

    @PostMapping(value = "/stock-transaction/add")
    public ServiceResponse<StockTransaction> addStockTransaction(@RequestBody AddOrUpdateStockTransaction stockTransaction)
    {
        ServiceResponse<StockTransaction> response = new ServiceResponse<>();
        if (stockTransaction.amount > 0) {
            ServiceResponse<Stock> stockResponse = stockService.getStockById(stockTransaction.stockId);
            if (!stockResponse.status){
                response.message = stockResponse.message;
                return response;
            }

            StockTransaction newStockTransaction = new StockTransaction();
            newStockTransaction.setAmount(stockTransaction.amount);
            newStockTransaction.setDescription(stockTransaction.description);
            newStockTransaction.setStock(stockResponse.data);
            newStockTransaction.setCreatedDate(new Date());
            //newStockTransaction.setCreatedBy();
            response = stockTransactionService.createStockTransaction(newStockTransaction);
        }
        else {
            response.message = "Gerekli olan alanları doldurunuz!";
        }
        return response;
    }

    @PutMapping(value = "/stock-transaction/update")
    public ServiceResponse<StockTransaction> updateStockTransaction(@RequestBody AddOrUpdateStockTransaction stockTransaction)
    {
        ServiceResponse<StockTransaction> response = new ServiceResponse<>();
        if (stockTransaction.amount > 0) {
            ServiceResponse<StockTransaction> stockTransactionResponse = stockTransactionService.getStockTransactionById(stockTransaction.id);
            if (!stockTransactionResponse.status) {
                response.message = stockTransactionResponse.message;
                return response;
            }

            ServiceResponse<Stock> stockResponse = stockService.getStockById(stockTransaction.stockId);
            if (!stockResponse.status){
                response.message = stockResponse.message;
                return response;
            }

            StockTransaction existing = stockTransactionResponse.data;
            existing.setAmount(stockTransaction.amount);
            existing.setDescription(stockTransaction.description);
            existing.setStock(stockResponse.data);
            existing.setUpdatedDate(new Date());
            //existing.setCreatedBy();
            response = stockTransactionService.updateStockTransaction(existing);
        }
        else {
            response.message = "Gerekli olan alanları doldurunuz!";
        }
        return response;
    }

    @GetMapping(value = "/stock-transaction/getStockTransactionById/{id}")
    public ServiceResponse<StockTransaction> getStockTransactionById(@PathVariable("id") UUID transactionId)
    {
        return stockTransactionService.getStockTransactionById(transactionId);
    }

    @GetMapping(value = "/stock-transaction/getStockTransactionsByStockId/{id}")
    public ServiceResponse<List<StockTransaction>> getStockTransactionsByStockId(@PathVariable("id") UUID stockId)
    {
        return stockTransactionService.getStockTransactions(stockId);
    }

    @GetMapping(value = "/stock-transaction/getStockTransactions")
    public ServiceResponse<List<StockTransaction>> getStockTransactions()
    {
        return stockTransactionService.getStockTransactions();
    }

    @DeleteMapping(value = "/stock-transaction/deleteStockTransactionById/{id}")
    public ServiceResponse<Boolean> deleteStockTransactionById(@PathVariable("id") UUID transactionId)
    {
        return stockTransactionService.deleteStockTransactionById(transactionId);
    }
}
