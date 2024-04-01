package com.seminav.marketapp.services;

import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.dtos.ProductDto;

public interface ProductService {

    ProductDto createProduct(CreateProductRequest createProductRequest);

    void changeProductStatus(String productId, String status);
}
