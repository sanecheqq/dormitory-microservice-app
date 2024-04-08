package com.seminav.marketapp.messages;

import com.seminav.marketapp.messages.dtos.ProductDto;

import java.util.List;

public record GetMyProductsResponse(
        List<ProductDto> products
) {}
