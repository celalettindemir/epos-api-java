package com.hineks.epos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ticket_items")
public class TicketItem extends BaseEntity {

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "description")
    private String description;

    @Column(name = "customerNumber", nullable = false)
    private int customerNumber;

    @Column(name = "isPaid", nullable = false)
    private boolean isPaid = false;

    @Column(name = "isPrinted", nullable = false)
    private boolean isPrinted = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticketId", nullable = false)
    private Ticket ticket;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinTable(
            name = "ticket_item_product_additions",
            joinColumns = { @JoinColumn(name = "ticketItemId") },
            inverseJoinColumns = { @JoinColumn(name = "productAdditionId") }
    )
    @JsonIgnore
    private Set<ProductAddition> productAdditions= new HashSet<>();
}
