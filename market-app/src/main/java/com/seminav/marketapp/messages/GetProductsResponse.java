package com.seminav.marketapp.messages;

import com.seminav.marketapp.messages.dtos.ProductDtoWithFavoriteField;

import java.util.List;

public record GetProductsResponse(
        List<ProductDtoWithFavoriteField> products // todo: пока тесчу - ProductDto, потом - ProductDtoWithFavoriteField
) {}
