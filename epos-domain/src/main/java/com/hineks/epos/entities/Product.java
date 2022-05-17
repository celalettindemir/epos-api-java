package com.hineks.epos.entities;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "products")
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productDetailId", nullable = false)
    private ProductDetail productDetail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mealId", nullable = false)
    private Meal meal;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "sidePrice")
    private double sidePrice;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<TicketItem> ticketItems;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ProductAddition> productAdditions;
}
