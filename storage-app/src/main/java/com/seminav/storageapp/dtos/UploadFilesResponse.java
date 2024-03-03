package com.seminav.storageapp.dtos;

import java.util.List;

public record UploadFilesResponse(
        List<FileDto> urls
) {}
