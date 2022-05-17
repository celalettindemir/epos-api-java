package com.hineks.epos.services;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.ProductAddition;
import com.hineks.epos.entities.Receipt;
import com.hineks.epos.entities.Ticket;
import com.hineks.epos.entities.TicketItem;
import com.hineks.epos.repositories.ReceiptsRepository;
import com.hineks.epos.repositories.TicketItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptsRepository receiptsRepository;

    private final TicketService ticketService;
    private final TicketItemService ticketItemService;
    public ServiceResponse<Receipt> createOrUpdateReceipt(Receipt receipt)
    {
        ServiceResponse<Receipt> response = new ServiceResponse<>();
        try {

            response.data = receiptsRepository.save(receipt);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
    public ServiceResponse<Receipt> getReceiptById(UUID id)
    {
        ServiceResponse<Receipt> response = new ServiceResponse<>();
        Optional<Receipt> ticketItem = Optional.empty();
        try {
            ticketItem = receiptsRepository.findById(id).filter(p -> !p.getIsDeleted());
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
    public ServiceResponse<Boolean> deleteReceiptItemById(UUID id)
    {
        ServiceResponse<Receipt> receipResponse = getReceiptById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (receipResponse.data != null) {
            try {
                //ProductDetail isDeleted islemi
                Receipt receipt = receipResponse.data;
                receipt.setIsDeleted(true);
                receiptsRepository.save(receipt);

                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = receipResponse.message;
        }
        return response;
    }
    public ServiceResponse<Receipt> createReceiptByTicketId(UUID ticketId)
    {
        ServiceResponse<Receipt> response = new ServiceResponse<>();
        Receipt receipt=new Receipt();
        try {
            Ticket ticket = ticketService.getTicketById(ticketId).data;
            List<TicketItem> ticketItem = ticketItemService.getTicketItemsByTicketId(ticketId).data;
            receipt.setTicket(ticket);
            double priceSum=0;
            for (TicketItem ticketItem1:ticketItem) {
                priceSum+=ticketItem1.getPrice();
            }
            receipt.setTotalPrice(priceSum);
            response.data=receipt;
            response.message = "Success!";
            response.status = true;

        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
    /*public ServiceResponse<Receipt> createOrUpdateReceiptByTicketList(UUID ticketId,List<TicketItem> ticketItemList)
    {
        ServiceResponse<Receipt> response = new ServiceResponse<>();
        try {
            Ticket ticket = ticketService.getTicketById(ticketId).data;
            List<TicketItem> ticketItem = ticketItemService.getTicketItemsByTicketId(ticketId).data;
            receipt.setTicket(ticket);
            double priceSum=0;
            for (TicketItem ticketItem1:ticketItem) {
                priceSum+=ticketItem1.getPrice();
            }
            receipt.setTotalPrice(priceSum);
            response.data=receipt;
            response.message = "Success!";
            response.status = true;

        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }*/
}
