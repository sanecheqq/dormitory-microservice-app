package com.seminav.newsapp.services;

import com.seminav.newsapp.messages.SortType;
import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.model.NewsCategory;

import java.util.List;

public interface NewsService {
    List<NewsDto> getNews(NewsCategory newsCategory, String searchPattern, SortType sortType);
}
