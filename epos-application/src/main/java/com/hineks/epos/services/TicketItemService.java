package com.hineks.epos.services;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.*;
import com.hineks.epos.repositories.TicketItemsRepository;
import com.hineks.epos.repositories.TicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketItemService {

    private final TicketItemsRepository ticketItemsRepository;
    
    public ServiceResponse<TicketItem> createOrUpdateTicketItem(TicketItem ticketItem)
    {
        ServiceResponse<TicketItem> response = new ServiceResponse<>();
        try {

            response.data = ticketItemsRepository.save(ticketItem);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
    public ServiceResponse<List<TicketItem>> createOrUpdateTicketItems(List<TicketItem> ticketItem)
    {
        ServiceResponse<List<TicketItem>> response = new ServiceResponse<>();
        try {

            response.data = (List<TicketItem>) ticketItemsRepository.saveAll(ticketItem);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
    public ServiceResponse<TicketItem> getTicketItemById(UUID id)
    {
        ServiceResponse<TicketItem> response = new ServiceResponse<>();
        Optional<TicketItem> ticketItem = Optional.empty();
        try {
            ticketItem = ticketItemsRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (ticketItem.isPresent()) {
            response.data = ticketItem.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }
    public ServiceResponse<Boolean> deleteTicketItemById(UUID id)
    {
        ServiceResponse<TicketItem> ticketItemResponse = getTicketItemById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (ticketItemResponse.data != null) {
            try {
                //ProductDetail isDeleted islemi
                TicketItem ticketItem = ticketItemResponse.data;
                ticketItem.setIsDeleted(true);
                ticketItemsRepository.save(ticketItem);

                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = ticketItemResponse.message;
        }
        return response;
    }
    public ServiceResponse<List<TicketItem>> getTicketItemsByTicketId(UUID ticketId){
        ServiceResponse<List<TicketItem>> response = new ServiceResponse<>();
        try {

            response.data = new ArrayList<TicketItem>(ticketItemsRepository.findByTicketId(ticketId));
            response.message = "Success!";
            response.status = true;

        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
    public ServiceResponse<TicketItem> createOrUpdateTicketItemAddition(UUID ticketItemId,List<ProductAddition> productAddition){
        ServiceResponse<TicketItem> response = new ServiceResponse<>();

        ServiceResponse<TicketItem> ticketItemResponse = getTicketItemById(ticketItemId);
        if (ticketItemResponse.data != null) {
            try {
                TicketItem ticketItem=ticketItemResponse.data;
                ticketItem.setProductAdditions((Set<ProductAddition>) productAddition);
                response.data = ticketItemsRepository.save(ticketItem);
                response.message = "Success!";
                response.status = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        return response;
    }
}
