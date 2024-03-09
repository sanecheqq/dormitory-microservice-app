package com.seminav.newsapp.messages;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdateNewsRequest(
        List<MultipartFile> images,
        List<MultipartFile> documents
) {
}
