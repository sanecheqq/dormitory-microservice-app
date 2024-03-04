package com.seminav.newsapp.messages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateNewsRequest(
    @NotBlank
    String title,
    @NotBlank
    String category,
    @NotBlank
    String content,
    @NotEmpty
    List<MultipartFile> images,
    @NotEmpty
    List<MultipartFile> documents
) {}
