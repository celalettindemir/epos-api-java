package com.hineks.epos.entities;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ticket_item_product_additionsjjjj")
public class TicketItemProductAddition extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productAdditionId", nullable = false)
    private ProductAddition productAddition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticketItemId", nullable = false)
    private TicketItem ticketItem;
}
