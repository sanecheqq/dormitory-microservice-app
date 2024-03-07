package com.seminav.newsapp.external.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileDto(
        @JsonProperty("file_id")
        String fileId,
        String url

) {}
