package com.seminav.marketapp.services;

import com.seminav.marketapp.external.messages.UserDto;
import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.GetProductsForValidationResponse;
import com.seminav.marketapp.messages.GetProductsResponse;
import com.seminav.marketapp.messages.PutProductRequest;
import com.seminav.marketapp.messages.dtos.ProductDto;
import com.seminav.marketapp.model.ProductCategory;

import java.util.List;
import java.util.Set;

public interface ProductService {

    ProductDto createProduct(CreateProductRequest createProductRequest, UserDto userDto);

    void changeProductStatus(String productId, String status);

    GetProductsForValidationResponse getProductsForValidation();

    void archiveProduct(String id);

    void deleteProduct(String id);

    GetProductsResponse getProducts(ProductCategory category, Double minPrice, Double maxPrice, String searchPattern, Integer page, Set<String> savedIds);

    ProductDto getProductById(String productId);

    void approveAllProducts();

    GetProductsResponse getFavoriteProducts(List<String> savedProducts);

    ProductDto putProduct(PutProductRequest putProductRequest, String id);
}
