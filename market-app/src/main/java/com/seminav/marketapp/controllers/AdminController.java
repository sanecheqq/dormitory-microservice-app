package com.seminav.marketapp.controllers;

import com.seminav.marketapp.exceptions.NotEnoughRootsException;
import com.seminav.marketapp.external.services.ExternalUserService;
import com.seminav.marketapp.messages.GetProductsForValidationResponse;
import com.seminav.marketapp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductService productService;
    private final ExternalUserService externalUserService;

    @PatchMapping("/product/{product_id}")
    public ResponseEntity<?> changeProductStatus(
            @PathVariable("product_id") String productId,
            @RequestParam(name = "status") String status,
            @RequestHeader("Authorization") String authorizationHeader

    ) {
        checkRoleOrElseThrow(externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader));
        productService.changeProductStatus(productId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validation")
    public ResponseEntity<GetProductsForValidationResponse> getProductsForValidation(@RequestHeader("Authorization") String authorizationHeader) {
        checkRoleOrElseThrow(externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader));
        return ResponseEntity.ok(productService.getProductsForValidation());
    }

    private void checkRoleOrElseThrow(String userRole) {
        if (userRole.equals("USER")) {
            throw new NotEnoughRootsException("Min required role is ADMIN. Your role is " + userRole);
        }
    }

}
