package com.seminav.marketapp.messages;

import com.seminav.marketapp.messages.dtos.ProductDto;

import java.util.List;

public record GetProductsResponse(
        List<ProductDto> products // todo: пока тесчу - ProductDto, потом - ProductDtoWithFavoriteField
) {}
