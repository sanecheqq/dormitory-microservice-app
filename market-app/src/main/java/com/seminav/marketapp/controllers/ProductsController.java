package com.seminav.marketapp.controllers;

import com.seminav.marketapp.external.services.ExternalUserService;
import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.GetProductsResponse;
import com.seminav.marketapp.messages.dtos.ProductDto;
import com.seminav.marketapp.model.ProductCategory;
import com.seminav.marketapp.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;
    private final ExternalUserService externalUserService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @ModelAttribute @Valid CreateProductRequest createProductRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader);
        ProductDto product = productService.createProduct(createProductRequest);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/sold/{id}")
    public ResponseEntity<?> archiveProduct(
            @PathVariable String id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader);
        productService.archiveProduct(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable String id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader);
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable("id") String productId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader);
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping()
    public ResponseEntity<GetProductsResponse> getProducts(
            @RequestParam(value = "search_pattern", required = false) String searchPattern,
            @RequestParam(value = "category", required = false) ProductCategory category,
            @RequestParam(value = "min_price", defaultValue = "0") Double minPrice,
            @RequestParam(value = "max_price", defaultValue = ""+1e7) Double maxPrice,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader);
        return ResponseEntity.ok(productService.getProducts(category, minPrice, maxPrice, searchPattern));
    }
}
