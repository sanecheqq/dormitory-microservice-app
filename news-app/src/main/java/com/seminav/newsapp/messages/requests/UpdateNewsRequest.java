package com.seminav.newsapp.messages.requests;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdateNewsRequest(
        List<MultipartFile> images,
        List<MultipartFile> documents
) {
}
