package com.hineks.epos.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "product_addition_stocks")
public class ProductAdditionStock extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productAdditionId", nullable = false)
    private ProductAddition productAddition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "StockId", nullable = false)
    private Stock stock;

    @Column(name = "consumedAmount", nullable = false)
    private double consumedAmount;
}
