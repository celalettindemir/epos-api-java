package com.hineks.epos.entities;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "allergens")
public class Allergen extends BaseEntity {

    @Column(name = "allergenName", nullable = false)
    private String allergenName;

    @Column(name = "icon")
    private String icon;

    @OneToMany(mappedBy = "allergen", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ProductAllergen> productAllergens;
}
