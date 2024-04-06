package com.seminav.marketapp.services;

import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.GetProductsForValidationResponse;
import com.seminav.marketapp.messages.dtos.ProductDto;

public interface ProductService {

    ProductDto createProduct(CreateProductRequest createProductRequest);

    void changeProductStatus(String productId, String status);

    GetProductsForValidationResponse getProductsForValidation();

    void archiveProduct(String id);

    void deleteProduct(String id);
}
