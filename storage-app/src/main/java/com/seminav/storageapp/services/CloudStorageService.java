package com.seminav.storageapp.services;

import com.seminav.storageapp.dtos.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudStorageService {
    List<FileDto> uploadFiles(List<MultipartFile> photos);

    void deleteFiles(List<String> filesIds);
}
