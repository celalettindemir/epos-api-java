package com.hineks.epos.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "receipts")
public class Receipt extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticketId", nullable = false)
    private Ticket ticket;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "attribute", nullable = false)
    private String attribute;

    @OneToMany(mappedBy = "receipt", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Payment> payments;
}
