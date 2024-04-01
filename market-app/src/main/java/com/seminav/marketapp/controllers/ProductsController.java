package com.seminav.marketapp.controllers;

import com.seminav.marketapp.external.services.ExternalUserService;
import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.dtos.ProductDto;
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
}
