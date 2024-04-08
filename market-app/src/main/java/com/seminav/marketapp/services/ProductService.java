package com.seminav.marketapp.services;

import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.GetProductsForValidationResponse;
import com.seminav.marketapp.messages.GetProductsResponse;
import com.seminav.marketapp.messages.dtos.ProductDto;
import com.seminav.marketapp.model.ProductCategory;

public interface ProductService {

    ProductDto createProduct(CreateProductRequest createProductRequest);

    void changeProductStatus(String productId, String status);

    GetProductsForValidationResponse getProductsForValidation();

    void archiveProduct(String id);

    void deleteProduct(String id);

    GetProductsResponse getProducts(ProductCategory category, Double minPrice, Double maxPrice, String searchPattern);
}
