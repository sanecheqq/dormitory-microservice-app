package com.seminav.marketapp.controllers;

import com.seminav.marketapp.external.messages.UserDto;
import com.seminav.marketapp.external.services.ExternalUserService;
import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.GetMyProductsResponse;
import com.seminav.marketapp.messages.GetProductsResponse;
import com.seminav.marketapp.messages.PutProductRequest;
import com.seminav.marketapp.messages.dtos.ProductDto;
import com.seminav.marketapp.model.ProductCategory;
import com.seminav.marketapp.services.ProductService;
import com.seminav.marketapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/market/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;
    private final UserService userService;
    private final ExternalUserService externalUserService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @ModelAttribute @Valid CreateProductRequest createProductRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        UserDto userDto = externalUserService.getUserDto(authorizationHeader);
        ProductDto product = productService.createProduct(createProductRequest, userDto);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> putProduct(
            @PathVariable("id") String id,
            @ModelAttribute @Valid PutProductRequest putProductRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader);
                return ResponseEntity.ok(productService.putProduct(putProductRequest, id));
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
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "search_pattern", required = false) String searchPattern,
            @RequestParam(value = "category", required = false) ProductCategory category,
            @RequestParam(value = "min_price", defaultValue = "0") Double minPrice,
            @RequestParam(value = "max_price", defaultValue = ""+1e7) Double maxPrice,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        List<String> savedProducts = externalUserService.getUserSavedProducts(authorizationHeader);
        return ResponseEntity.ok(productService.getProducts(category, minPrice, maxPrice, searchPattern, page, new HashSet<>(savedProducts)));
    }

    @GetMapping("/favorites")
    public ResponseEntity<GetProductsResponse> getFavoritesProducts(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        List<String> savedProducts = externalUserService.getUserSavedProducts(authorizationHeader);
        return ResponseEntity.ok(productService.getFavoriteProducts(savedProducts));
    }

    @GetMapping("/my")
    public ResponseEntity<GetMyProductsResponse> getMyProducts(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        UserDto userDto = externalUserService.getUserDto(authorizationHeader);
        return ResponseEntity.ok(userService.getProducts(userDto.id()));
    }
}
