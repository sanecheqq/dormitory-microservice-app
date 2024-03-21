package com.seminav.storageapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileDto(
        @JsonProperty("file_id")
        String fileId,
        @JsonProperty("file_name")
        String fileName,
        String url
) {}
