package com.seminav.newsapp.messages.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewsDto(
   String id,
   String title,
   String category,
   String content,
   @JsonProperty(value = "image_id")
   String imageId,
   String date
) {}
