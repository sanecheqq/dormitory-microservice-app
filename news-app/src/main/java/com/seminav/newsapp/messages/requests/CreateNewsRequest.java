package com.seminav.newsapp.messages.requests;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateNewsRequest(
    @NotBlank
    String title,
    @NotBlank
    String category,
    @NotBlank
    String content,
    List<MultipartFile> images,
    List<MultipartFile> documents
) {}
