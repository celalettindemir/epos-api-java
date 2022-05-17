package com.hineks.epos.definitions;

import com.hineks.epos.entities.Ticket;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.util.UUID;

@Getter
@Setter
public class AddOrUpdateReceipt {
    public UUID id;
    public UUID ticketId;
    public double totalPrice;
    public String attribute;
}
