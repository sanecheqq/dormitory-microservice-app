package com.seminav.newsapp.external.services;


import com.seminav.newsapp.external.messages.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudStorageService {
    List<FileDto> uploadFiles(List<MultipartFile> files);
    void deleteFiles(List<String> ids);
}
