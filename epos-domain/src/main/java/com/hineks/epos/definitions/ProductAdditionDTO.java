package com.hineks.epos.definitions;

import java.util.*;

public class ProductAdditionDTO {
    public UUID id;
    public String name;
    public double price;
    public boolean isDefault;
    public List<ProductAdditionStockDTO> productAdditionStocks;
}
