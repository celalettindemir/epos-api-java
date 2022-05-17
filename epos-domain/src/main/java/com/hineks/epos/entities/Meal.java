package com.hineks.epos.entities;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "meals")
public class Meal extends BaseEntity {

    @Column(name = "mealName", nullable = false)
    private String mealName;

    @OneToMany(mappedBy = "meal", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Product> products;
}
