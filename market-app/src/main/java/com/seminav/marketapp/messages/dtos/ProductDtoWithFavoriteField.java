package com.seminav.marketapp.messages.dtos;

import com.seminav.marketapp.external.messages.FileDto;

import java.util.List;

public record ProductDtoWithFavoriteField(
        String id,
        String name,
        String category,
        String description,
        double price,
        String date,
        String address,
        List<FileDto> images,
        Boolean favorite
) {}
