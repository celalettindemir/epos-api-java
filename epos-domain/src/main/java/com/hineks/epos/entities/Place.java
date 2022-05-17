package com.hineks.epos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "places")
public class Place extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Table> tables;
}
