package com.hineks.epos.definitions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddOrUpdatePaymentType {
    public UUID id;
    public String typeName;
}
