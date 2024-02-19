package com.seminav.newsapp.services;

import com.seminav.newsapp.messages.SortType;
import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.model.News;
import com.seminav.newsapp.model.NewsCategory;
import com.seminav.newsapp.repositories.NewsRepository;
import com.seminav.newsapp.util.converters.NewsToNewsDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepo;
    private final NewsToNewsDtoConverter newsToNewsDtoConverter;

    @Override
    public List<NewsDto> getNews(NewsCategory newsCategory, String searchPattern, SortType sortType) {
        var sortOrder = sortType.equals(SortType.ASCENDING) ? Sort.Order.asc("date") : Sort.Order.desc("date");
        List<News> news = newsRepo.getNewsByNewsCategoryAndSearchPatternAndSortByDate(newsCategory.getCategory(), searchPattern, Sort.by(sortOrder));
        return news.stream()
                .map(newsToNewsDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
