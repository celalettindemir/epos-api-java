package com.hineks.epos.definitions;

import java.util.*;

public class ProductDetailDTO {
    public UUID id;
    public UUID categoryId;
    public UUID taxCategoryId;
    public String name;
    public String color;
    public String image;
    public String description;
    public boolean isVegan = false;
    public boolean isGluten = false;
    public boolean isSide = false;
    public List<ProductDetailDefaultSideDTO> productDetailDefaultSides;
    public List<ProductAllergenDTO> productAllergens;
    public List<ProductDTO> products;
}
