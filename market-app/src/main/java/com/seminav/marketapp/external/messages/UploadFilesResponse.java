package com.seminav.marketapp.external.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UploadFilesResponse(
        @JsonProperty("file_dtos")
        List<FileDto> fileDtos
) {
}
