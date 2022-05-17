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
public class StockController {
    private final StockService stockService;

    @PostMapping(value = "/stock/add")
    public ServiceResponse<Stock> addStock(@RequestBody AddOrUpdateStock stock)
    {
        ServiceResponse<Stock> response = new ServiceResponse<>();
        if (stock.name.length() > 0 && stock.type > 0) {
            Stock newStock = new Stock();
            newStock.setName(stock.name);
            newStock.setAmount(0);
            newStock.setType(stock.type);
            newStock.setCreatedDate(new Date());
            //place.setCreatedBy();
            response = stockService.createOrUpdateStock(newStock);
        }
        else {
            response.message = "Gerekli olan alanları doldurunuz!";
        }
        return response;
    }

    @PutMapping(value = "/stock/update")
    public ServiceResponse<Stock> updateStock(@RequestBody AddOrUpdateStock stock)
    {
        ServiceResponse<Stock> response = new ServiceResponse<>();
        if (stock.name.length() > 0 && stock.type > 0){
            ServiceResponse<Stock> getStockResponse = stockService.getStockById(stock.id);
            if (!getStockResponse.status)
                return getStockResponse;

            Stock existing = getStockResponse.data;
            existing.setName(stock.name);
            existing.setType(stock.type);
            existing.setUpdatedDate(new Date());
            return stockService.createOrUpdateStock(existing);
        }
        else {
            response.message = "Gerekli alanları doldurunuz!";
        }
        return response;
    }

    @GetMapping(value = "/stock/getStockById/{id}")
    public ServiceResponse<Stock> getStockById(@PathVariable("id") UUID stockId)
    {
        return stockService.getStockById(stockId);
    }

    @GetMapping(value = "/stock/getStocks")
    public ServiceResponse<List<Stock>> getStocks()
    {
        return stockService.getStocks();
    }

    @DeleteMapping(value = "/stock/deleteStockById/{id}")
    public ServiceResponse<Boolean> deleteStockById(@PathVariable("id") UUID stockId)
    {
        return stockService.deleteStockById(stockId);
    }
}
