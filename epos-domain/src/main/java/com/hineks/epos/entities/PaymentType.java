package com.hineks.epos.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "payment_types")
public class   PaymentType extends  BaseEntity {


    @Column(name = "typeName", nullable = false)
    private String typeName;

    @OneToMany(mappedBy = "paymentType", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Payment> payments;
}
