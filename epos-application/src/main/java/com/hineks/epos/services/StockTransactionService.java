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
public class StockTransactionService {
    private final StockTransactionsRepository stockTransactionsRepository;
    private final StockService stockService;

    @Autowired
    public StockTransactionService(StockTransactionsRepository stockTransactionsRepository,
                                   @Lazy StockService stockService)
    {
        this.stockService = stockService;
        this.stockTransactionsRepository = stockTransactionsRepository;
    }

    public ServiceResponse<StockTransaction> createStockTransaction(StockTransaction stockTransaction)
    {
        ServiceResponse<StockTransaction> response = new ServiceResponse<>();
        try {
            StockTransaction createdTransaction = stockTransactionsRepository.save(stockTransaction);
            Stock stock = createdTransaction.getStock();
            stock.setAmount(stock.getAmount() + createdTransaction.getAmount());

            ServiceResponse<Stock> updatedStockResponse = stockService.createOrUpdateStock(stock);
            if (updatedStockResponse.status) {
                response.data = createdTransaction;
                response.status = true;
                response.message = "Success!";
            }
            else {
                response.message = "Kayıt eklendi fakat stok miktarı güncellenirken bir hata oluştu. Son eklediğiniz kaydı silip tekrar deneyiniz. Hata: "
                        + updatedStockResponse.message;
            }
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<StockTransaction> updateStockTransaction(StockTransaction stockTransaction)
    {
        ServiceResponse<StockTransaction> response = new ServiceResponse<>();
        try {
            ServiceResponse<StockTransaction> oldTransactionResponse = getStockTransactionById(stockTransaction.getId());
            if (oldTransactionResponse.status) {
                StockTransaction updatedTransaction = stockTransactionsRepository.save(stockTransaction);
                Stock stock = updatedTransaction.getStock();
                double diff = updatedTransaction.getAmount() - oldTransactionResponse.data.getAmount();
                if (diff > 0) {
                    stock.setAmount(stock.getAmount() + diff);
                }
                else if (diff < 0) {
                    stock.setAmount(stock.getAmount() - diff);
                }

                ServiceResponse<Stock> updatedStockResponse = stockService.createOrUpdateStock(stock);
                if (updatedStockResponse.status) {
                    response.data = updatedTransaction;
                    response.status = true;
                    response.message = "Success!";
                }
                else {
                    response.message = "Kayıt güncellendi fakat stok miktarı güncellenirken bir hata oluştu. Hata: "
                            + updatedStockResponse.message;
                }
            }
            response.message = oldTransactionResponse.message;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<StockTransaction> getStockTransactionById(UUID id)
    {
        ServiceResponse<StockTransaction> response = new ServiceResponse<>();
        Optional<StockTransaction> stockTransaction = Optional.empty();
        try {
            stockTransaction = stockTransactionsRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (stockTransaction.isPresent()) {
            response.data = stockTransaction.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteStockTransactionById(UUID id)
    {
        ServiceResponse<StockTransaction> stockTransactionResponse = getStockTransactionById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (stockTransactionResponse.data != null) {
            try {
                //StockTransaction isDeleted islemi
                StockTransaction stockTransaction = stockTransactionResponse.data;
                stockTransaction.setIsDeleted(true);
                stockTransactionsRepository.save(stockTransaction);

                //Stocktan miktarı düşme işlemi
                Stock stock = stockTransaction.getStock();
                stock.setAmount(stock.getAmount() - stockTransaction.getAmount());
                ServiceResponse<Stock> stockResponse = stockService.createOrUpdateStock(stock);
                if (stockResponse.status) {
                    //Response
                    response.status = true;
                    response.message = "Success!";
                    response.data = true;
                }
                else {
                    response.message = "Kayıt silindi fakat stok miktarı düşülürken bir hata oluştu. Hata: "
                            + stockResponse.message;
                }
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = stockTransactionResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<StockTransaction>> getStockTransactions()
    {
        ServiceResponse<List<StockTransaction>> response = new ServiceResponse<>();
        try {
            response.data = stockTransactionsRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<List<StockTransaction>> getStockTransactions(UUID stockId)
    {
        ServiceResponse<List<StockTransaction>> response = new ServiceResponse<>();
        try {
            ServiceResponse<Stock> stockResponse = stockService.getStockById(stockId);
            if (stockResponse.status) {
                response.data = new ArrayList<StockTransaction>(stockResponse.data.getStockTransactions());
                response.message = "Success!";
                response.status = true;
            }
            else {
                response.message = stockResponse.message;
            }
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
