package com.hineks.epos.services;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.Product;
import com.hineks.epos.entities.ProductDetail;
import com.hineks.epos.entities.Ticket;
import com.hineks.epos.entities.TicketItem;
import com.hineks.epos.repositories.ProductsRepository;
import com.hineks.epos.repositories.TicketItemsRepository;
import com.hineks.epos.repositories.TicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketsRepository ticketsRepository;
    private final TicketItemsRepository ticketItemsRepository;
    private final TicketItemService ticketItemService;
    public ServiceResponse<Ticket> createOrUpdateTicket(Ticket ticket)
    {
        ServiceResponse<Ticket> response = new ServiceResponse<>();
        try {

            response.data = ticketsRepository.save(ticket);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<Ticket> getTicketById(UUID id)
    {
        ServiceResponse<Ticket> response = new ServiceResponse<>();
        Optional<Ticket> ticket = Optional.empty();
        try {
            ticket = ticketsRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (ticket.isPresent()) {
            response.data = ticket.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteTicketById(UUID id)
    {
        ServiceResponse<Ticket> productResponse = getTicketById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (productResponse.data != null) {
            try {
                //Product isDeleted işlemi
                Ticket ticket = productResponse.data;
                ticket.setIsDeleted(true);
                ticketsRepository.save(ticket);

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

    public ServiceResponse<List<Ticket>> getTickets()
    {
        ServiceResponse<List<Ticket>> response = new ServiceResponse<>();
        try {
            response.data = ticketsRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
    public ServiceResponse<Ticket> createTicketandUpdateTicketItem(UUID id,List<UUID> uuids,Ticket ticket)
    {
        ServiceResponse<Ticket> response = new ServiceResponse<>();
        try {
            List<TicketItem> ticketItems =ticketItemService.getTicketItemsByTicketId(id).data;
            List<TicketItem> ticketItems1=new ArrayList<>();
            ticket=ticketsRepository.save(ticket);
            for (TicketItem ticketItem:ticketItems) {
                if(uuids.indexOf(ticketItem.getId())!=-1){
                    ticketItem.setTicket(ticket);
                    ticketItems1.add(ticketItem);
                }
            }
            ticketItemsRepository.saveAll(ticketItems1);
            response.data =  ticket;
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
