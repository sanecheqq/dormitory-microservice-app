package com.seminav.newsapp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateNewsRequest(
    String title,
    String category,
    String content,
    @JsonProperty(value = "image_id")
    String imageId
) {}
