package com.seminav.marketapp.external.services;


import com.seminav.marketapp.external.messages.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudStorageService {
    List<FileDto> uploadFiles(List<MultipartFile> files);

    void deleteFiles(List<String> ids);
}
