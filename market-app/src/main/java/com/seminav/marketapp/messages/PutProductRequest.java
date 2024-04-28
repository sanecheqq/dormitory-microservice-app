package com.seminav.marketapp.messages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PutProductRequest(
        @NotBlank
        String name,
        @NotBlank
        String category,
        @NotBlank
        String description,
        Double price,
        List<String> oldImages,
        @NotEmpty
        List<MultipartFile> newImages,
        String address
) {
}
