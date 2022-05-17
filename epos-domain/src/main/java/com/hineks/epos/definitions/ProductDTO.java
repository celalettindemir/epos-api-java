package com.hineks.epos.definitions;

import java.util.*;

public class ProductDTO {
    public UUID id;
    public UUID mealId;
    public double price;
    public double sidePrice;
    public List<ProductAdditionDTO> productAdditions;
}
