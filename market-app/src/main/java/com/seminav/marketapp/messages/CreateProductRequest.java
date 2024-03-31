package com.seminav.marketapp.messages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateProductRequest(
    @NotBlank
    String name,
    @NotBlank
    String category,
    @NotBlank
    String description,
    @NotBlank
    double price,
    @NotEmpty
    List<MultipartFile> images
) {}
