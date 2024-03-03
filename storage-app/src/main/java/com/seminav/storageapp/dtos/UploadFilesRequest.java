package com.seminav.storageapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UploadFilesRequest(
        @NotEmpty
        List<MultipartFile> files
) {
}
