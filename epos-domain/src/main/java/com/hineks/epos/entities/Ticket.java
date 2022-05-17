package com.hineks.epos.entities;

import com.hineks.epos.definitions.TicketStatus;
import com.hineks.epos.definitions.TicketType;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tickets")
public class Ticket extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10,nullable = false)
    private TicketType type=TicketType.TABLE;

    public void setType(TicketType ticketType)
    {
        type=ticketType;
    }

    public void setType(String ticketType)
    {
        if(ticketType=="TABLE")
            type=TicketType.TABLE;
        if(ticketType=="TAKEAWAY")
            type=TicketType.TAKEAWAY;
        if(ticketType=="ONLINE")
            type=TicketType.ONLINE;
    }
    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Receipt> receipts;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<TicketItem> ticketItems;
}
