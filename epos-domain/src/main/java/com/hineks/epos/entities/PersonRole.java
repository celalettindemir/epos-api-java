package com.hineks.epos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "people_roles")
public class PersonRole extends BaseEntity {

    @Column(name = "roleName")
    private String Name;

    @OneToMany(mappedBy = "personRole", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Person> people;

}
