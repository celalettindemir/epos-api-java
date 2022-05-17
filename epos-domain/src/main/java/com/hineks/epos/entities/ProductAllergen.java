package com.hineks.epos.entities;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "product_allergens")
public class ProductAllergen extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "allergenId", nullable = false)
    private Allergen allergen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productDetailId", nullable = false)
    private ProductDetail productDetail;
}
