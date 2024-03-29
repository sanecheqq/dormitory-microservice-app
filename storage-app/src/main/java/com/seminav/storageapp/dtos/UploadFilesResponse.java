package com.seminav.storageapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UploadFilesResponse(
        @JsonProperty("file_dtos")
        List<FileDto> fileDtos
) {}
