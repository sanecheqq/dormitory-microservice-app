package com.seminav.newsapp.messages;

import com.seminav.newsapp.messages.dtos.NewsDto;

import java.util.List;

public record GetSavedNewsResponse(
   List<NewsDto> news
) {}
