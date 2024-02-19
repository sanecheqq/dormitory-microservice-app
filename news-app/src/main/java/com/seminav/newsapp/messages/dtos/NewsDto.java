package com.seminav.newsapp.messages.dtos;

public record NewsDto(
   String id,
   String title,
   String category,
   String content,
   String imageId,
   String date
) {}
