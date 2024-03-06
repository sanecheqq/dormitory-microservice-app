package com.seminav.newsapp.services;

import com.seminav.newsapp.messages.CreateNewsRequest;
import com.seminav.newsapp.messages.SortType;
import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.model.NewsCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsService {
    List<NewsDto> getNews(NewsCategory newsCategory, String searchPattern, SortType sortType);

    List<NewsDto> getSavedNews(List<String> strings);

    NewsDto createNews(CreateNewsRequest createNewsRequest);

    void deleteNews(String newsId);

    NewsDto updateNews(String newsId, String title, String category, String content, List<MultipartFile> images, List<MultipartFile> documents);
}
