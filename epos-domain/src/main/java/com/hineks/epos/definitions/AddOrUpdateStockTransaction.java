package com.hineks.epos.definitions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddOrUpdateStockTransaction {
    public UUID id;
    public UUID stockId;
    public String description;
    public double amount;
}
