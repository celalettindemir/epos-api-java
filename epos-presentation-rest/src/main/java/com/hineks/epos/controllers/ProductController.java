package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductDetailService productDetailService;
    private final ProductCategoryService productCategoryService;
    private final ProductDetailDefaultSideService productDetailDefaultSideService;
    private final ProductService productService;
    private final ProductAllergenService productAllergenService;
    private final ProductAdditionService productAdditionService;
    private final ProductAdditionStockService productAdditionStockService;
    private final AllergenService allergenService;
    private final MealService mealService;
    private final StockService stockService;
    private final TaxCategoryService taxCategoryService;

    @PostMapping(value = "/product/addOrUpdate")
    public ServiceResponse<ProductDetail> addOrUpdateProduct(@RequestBody ProductDetailDTO newProductDetail)
    {
        ServiceResponse<ProductDetail> response = new ServiceResponse<>();
        if (newProductDetail.name.length() > 0) {
            //Category control
            ServiceResponse<ProductCategory> isProductCategoryExist = productCategoryService.getProductCategoryById(newProductDetail.categoryId);
            if (!isProductCategoryExist.status) {
                response.message = isProductCategoryExist.message;
                return response;
            }

            //TaxCategory control
            ServiceResponse<TaxCategory> isTaxCategoryExist = taxCategoryService.getTaxCategoryById(newProductDetail.taxCategoryId);
            if (!isTaxCategoryExist.status) {
                response.message = isTaxCategoryExist.message;
                return response;
            }

            //ProductDetail create-update işlemi
            ProductDetail productDetail;
            ServiceResponse<ProductDetail> isProductDetailExist = productDetailService.getProductDetailById(newProductDetail.id);
            if (isProductDetailExist.status) {
                productDetail = isProductDetailExist.data;
            }
            else {
                productDetail = new ProductDetail();
            }
            productDetail.setCreatedDate(new Date());
            productDetail.setVegan(newProductDetail.isVegan);
            productDetail.setGluten(newProductDetail.isGluten);
            productDetail.setSide(newProductDetail.isSide);
            productDetail.setProductName(newProductDetail.name);
            productDetail.setColor(newProductDetail.color);
            productDetail.setImage(newProductDetail.image);
            productDetail.setDescription(newProductDetail.description);
            productDetail.setCategory(isProductCategoryExist.data);
            productDetail.setTaxCategory(isTaxCategoryExist.data);
            ServiceResponse<ProductDetail> addedProductDetailResponse = productDetailService.createOrUpdateProductDetail(productDetail);

            if (!addedProductDetailResponse.status) {
                response.message = addedProductDetailResponse.message;
                return response;
            }

            //ProductDetail DefaultSide create-update işlemi
            for (ProductDetailDefaultSideDTO pdds :
                    newProductDetail.productDetailDefaultSides) {
                ServiceResponse<ProductDetail> isSideProductExistControl = productDetailService.getProductDetailById(pdds.sideProductId);
                if (isSideProductExistControl.status) {
                    ProductDetailDefaultSide newPdds;
                    ServiceResponse<ProductDetailDefaultSide> isPddsExist = productDetailDefaultSideService.getProductDetailDefaultSideById(pdds.id);
                    if (isPddsExist.status) {
                        newPdds = isPddsExist.data;
                    }
                    else{
                        newPdds = new ProductDetailDefaultSide();
                    }
                    newPdds.setProductDetail(addedProductDetailResponse.data);
                    newPdds.setSideProductDetail(isSideProductExistControl.data);
                    productDetailDefaultSideService.createOrUpdateProductDetailDefaultSide(newPdds);
                }
            }

            //ProductAllergen create-update işlemi
            for (ProductAllergenDTO productAllergen :
                    newProductDetail.productAllergens) {
                ServiceResponse<Allergen> isAllergenExist = allergenService.getAllergenById(productAllergen.allergenId);
                if (isAllergenExist.status) {
                    ProductAllergen newProductAllergen;
                    ServiceResponse<ProductAllergen> isProductAllergenExist = productAllergenService.getProductAllergenById(productAllergen.id);
                    if (isProductAllergenExist.status) {
                        newProductAllergen = isProductAllergenExist.data;
                    }
                    else {
                        newProductAllergen = new ProductAllergen();
                    }
                    newProductAllergen.setCreatedDate(new Date());
                    newProductAllergen.setProductDetail(addedProductDetailResponse.data);
                    newProductAllergen.setAllergen(isAllergenExist.data);
                    productAllergenService.createOrUpdateProductAllergen(newProductAllergen);
                }
            }

            //Product ve alt tabloları için create-update işlemi
            for (ProductDTO product :
                    newProductDetail.products) {
                ServiceResponse<Meal> isMealExist = mealService.getMealById(product.mealId);
                //Gönderilen mealId ile eşleşen meal varsa
                if (isMealExist.status) {
                    //Product eklenir/güncellenir
                    Product newProduct;
                    ServiceResponse<Product> isProductExist = productService.getProductById(product.id);
                    if (isProductExist.status) {
                        newProduct = isProductExist.data;
                    }
                    else {
                        newProduct = new Product();
                    }
                    newProduct.setMeal(isMealExist.data);
                    newProduct.setPrice(product.price);
                    newProduct.setProductDetail(addedProductDetailResponse.data);
                    newProduct.setSidePrice(product.sidePrice);
                    newProduct.setCreatedDate(new Date());
                    ServiceResponse<Product> addedProduct = productService.createOrUpdateProduct(newProduct);

                    //Product ekleme/güncelleme başarılıysa
                    if (addedProduct.status) {
                        for (ProductAdditionDTO productAddition :
                                product.productAdditions) {
                            //Product addition eklenir/güncellenir
                            ProductAddition newProductAddition;
                            ServiceResponse<ProductAddition> isProductAdditionExist = productAdditionService.getProductAdditionById(productAddition.id);
                            if (isProductAdditionExist.status) {
                                newProductAddition = isProductAdditionExist.data;
                            }
                            else {
                                newProductAddition = new ProductAddition();
                            }
                            newProductAddition.setDefault(productAddition.isDefault);
                            newProductAddition.setName(productAddition.name);
                            newProductAddition.setPrice(productAddition.price);
                            newProductAddition.setProduct(addedProduct.data);
                            newProductAddition.setCreatedDate(new Date());
                            ServiceResponse<ProductAddition> addedProductAddition = productAdditionService.createOrUpdateProductAddition(newProductAddition);

                            //ProductAddition ekleme/güncelleme başarılıysa
                            if (addedProductAddition.status) {
                                for (ProductAdditionStockDTO pas :
                                        productAddition.productAdditionStocks) {
                                    ServiceResponse<Stock> isStockExist = stockService.getStockById(pas.stockId);
                                    //Gönderilen stockId ile eşleşen stock varsa
                                    if (isStockExist.status) {
                                        //ProductAdditionStock eklenir/güncellenir
                                        ProductAdditionStock newPas;
                                        ServiceResponse<ProductAdditionStock> isProductAdditionStockExist = productAdditionStockService.getProductAdditionStockById(pas.id);
                                        if (isProductAdditionStockExist.status) {
                                            newPas = isProductAdditionStockExist.data;
                                        }
                                        else {
                                            newPas = new ProductAdditionStock();
                                        }
                                        newPas.setStock(isStockExist.data);
                                        newPas.setProductAddition(addedProductAddition.data);
                                        newPas.setConsumedAmount(pas.consumedAmount);
                                        newPas.setCreatedDate(new Date());
                                        productAdditionStockService.createOrUpdateProductAdditionStock(newPas);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            response = addedProductDetailResponse;
        }
        else {
            response.message = "Ürün adı girmediniz!";
        }
        return response;
    }

    @GetMapping(value = "/product/getProductDetailById/{id}")
    public ServiceResponse<ProductDetailDTO> getProductDetailById(@PathVariable("id") UUID id)
    {
        ServiceResponse<ProductDetailDTO> response = new ServiceResponse<>();
        ServiceResponse<ProductDetail> productDetailResponse = productDetailService.getProductDetailById(id);
        if (!productDetailResponse.status) {
            response.message = productDetailResponse.message;
            return response;
        }

        ProductDetail productDetail = productDetailResponse.data;

        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.categoryId = productDetail.getCategory().getId();
        productDetailDTO.description = productDetail.getDescription();
        productDetailDTO.id = productDetail.getId();
        productDetailDTO.image = productDetail.getImage();
        productDetailDTO.isSide = productDetail.isSide();
        productDetailDTO.isVegan = productDetail.isVegan();
        productDetailDTO.isGluten = productDetail.isGluten();
        productDetailDTO.color = productDetail.getColor();
        productDetailDTO.name = productDetail.getProductName();
        productDetailDTO.taxCategoryId = productDetail.getTaxCategory().getId();
        productDetailDTO.products = new ArrayList<>();
        productDetailDTO.productAllergens = new ArrayList<>();
        productDetailDTO.productDetailDefaultSides = new ArrayList<>();

        for (ProductDetailDefaultSide pdds :
                productDetail.getSidesOfProduct()) {
            ProductDetailDefaultSideDTO pddsDTO = new ProductDetailDefaultSideDTO();
            pddsDTO.id = pdds.getId();
            pddsDTO.sideProductId = pdds.getSideProductDetail().getId();
            productDetailDTO.productDetailDefaultSides.add(pddsDTO);
        }

        for (ProductAllergen productAllergen :
                productDetail.getProductAllergens()) {
            ProductAllergenDTO paDTO = new ProductAllergenDTO();
            paDTO.id = productAllergen.getId();
            paDTO.allergenId = productAllergen.getAllergen().getId();
            productDetailDTO.productAllergens.add(paDTO);
        }

        for (Product product :
                productDetail.getProducts()) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.id = product.getId();
            productDTO.mealId = product.getMeal().getId();
            productDTO.price = product.getPrice();
            productDTO.sidePrice = product.getSidePrice();
            productDTO.productAdditions = new ArrayList<>();
            for (ProductAddition productAddition :
                    product.getProductAdditions()) {
                ProductAdditionDTO paDTO = new ProductAdditionDTO();
                paDTO.id = productAddition.getId();
                paDTO.isDefault = productAddition.isDefault();
                paDTO.name = productAddition.getName();
                paDTO.price = productAddition.getPrice();
                paDTO.productAdditionStocks = new ArrayList<>();
                for (ProductAdditionStock pas :
                        productAddition.getProductAdditionStocks()) {
                    ProductAdditionStockDTO pasDTO = new ProductAdditionStockDTO();
                    pasDTO.consumedAmount = pas.getConsumedAmount();
                    pasDTO.id = pas.getId();
                    pasDTO.stockId = pas.getStock().getId();
                    paDTO.productAdditionStocks.add(pasDTO);
                }
                productDTO.productAdditions.add(paDTO);
            }
            productDetailDTO.products.add(productDTO);
        }

        response.status = true;
        response.message = "Success!";
        response.data = productDetailDTO;
        return response;
    }

    @DeleteMapping(value = "/product/deleteProductDetailById/{id}")
    public ServiceResponse<Boolean> deleteProductDetailById(@PathVariable("id") UUID id)
    {
        return productDetailService.deleteProductDetailById(id);
    }
}
