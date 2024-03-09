package com.seminav.newsapp.messages.dtos;

import com.seminav.newsapp.external.messages.FileDto;

import java.util.List;

public record NewsDto(
   String id,
   String title,
   String category,
   String content,
   List<FileDto> images,
   List<FileDto> documents,
   String date
) {}
