package com.hineks.epos.definitions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddOrUpdateAllergen {
    public UUID id;
    public String name;
    public String icon;
}
