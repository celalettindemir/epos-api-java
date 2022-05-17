package com.hineks.epos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "product_detail")
public class ProductDetail extends BaseEntity {

    @Column(name = "productName", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    //değişecek -> byte[]
    @Column(name = "image")
    private String image;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "isVegan", nullable = false)
    private boolean isVegan;

    @Column(name = "isGluten", nullable = false)
    private boolean isGluten;

    @Column(name = "isSide", nullable = false)
    private boolean isSide;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId", nullable = false)
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxCategoryId")
    private TaxCategory taxCategory;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ProductAllergen> productAllergens;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Product> products;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ProductDetailDefaultSide> SidesOfProduct;

    @OneToMany(mappedBy = "sideProductDetail", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ProductDetailDefaultSide> ProductSides;
}
