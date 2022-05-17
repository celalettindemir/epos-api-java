package com.hineks.epos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tax_categories")
public class TaxCategory extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rate", nullable = false)
    private double rate;

    @OneToMany(mappedBy = "taxCategory", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ProductDetail> productDetails;
}
