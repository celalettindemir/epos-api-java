package com.hineks.epos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "stock")
public class Stock extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private double amount = 0.0;

    @Column(name = "type", nullable = false)
    private int type;

    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<StockTransaction> stockTransactions;

    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ProductAdditionStock> productAdditionStocks;
}
