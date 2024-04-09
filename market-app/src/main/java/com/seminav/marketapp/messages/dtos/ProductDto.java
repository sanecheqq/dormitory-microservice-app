package com.seminav.marketapp.messages.dtos;

import com.seminav.marketapp.external.messages.FileDto;

import java.util.List;

public record ProductDto(
        String id,
        String name,
        String category,
        String description,
        double price,
        String date,
        String address,
        String tgUsername,
        String status,
        List<FileDto> images
) {}
