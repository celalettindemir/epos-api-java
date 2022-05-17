package com.hineks.epos.definitions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddOrUpdateProductStock {
    public UUID id;
    public UUID stockId;
    public UUID productId;
    public String name;
    public double price = 0.0;
    public double consumedAmount = 0.0;
    public boolean isDefault = false;
}
