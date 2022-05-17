package com.hineks.epos.definitions;

import com.hineks.epos.entities.Product;
import com.hineks.epos.entities.Ticket;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Getter
@Setter
public class AddOrUpdateTicketItem {
    public UUID id;
    public int count;
    public double price;
    public String description;
    public int customerNumber;
    public boolean isPaid;
    public UUID productId;
    public UUID ticketId;

}
