package com.seminav.marketapp.messages;

import com.seminav.marketapp.messages.dtos.ProductDtoWithFavoriteField;

import java.util.List;

public record GetSavedProductsResponse(
   List<ProductDtoWithFavoriteField> products
) {}
