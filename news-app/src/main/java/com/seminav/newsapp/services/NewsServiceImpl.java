package com.seminav.newsapp.services;

import com.seminav.newsapp.messages.CreateNewsRequest;
import com.seminav.newsapp.messages.SortType;
import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.model.News;
import com.seminav.newsapp.model.NewsCategory;
import com.seminav.newsapp.repositories.NewsRepository;
import com.seminav.newsapp.util.converters.NewsToNewsDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
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
        List<News> news = newsRepo.getNewsByNewsCategoryAndSearchPatternAndSortByDate(newsCategory, searchPattern, Sort.by(sortOrder));
        return convertListNewsToListNewsDto(news);
    }

    @Override
    public List<NewsDto> getSavedNews(List<String> idsOfNews) {
        List<News> savedNews = newsRepo.findAllById(idsOfNews);
        return convertListNewsToListNewsDto(savedNews);
    }

    @Override
    public NewsDto createNews(CreateNewsRequest createNewsRequest) {
        News news = new News();
        news.setTitle(createNewsRequest.title());
        news.setCategory(NewsCategory.valueOf(createNewsRequest.category()));
        news.setContent(createNewsRequest.content());
        news.setDate(Timestamp.from(Instant.now()));
        news.setImageId(createNewsRequest.imageId());
        return newsToNewsDtoConverter.convert(newsRepo.save(news));
    }

    private List<NewsDto> convertListNewsToListNewsDto(List<News> news) {
        return news.stream()
                .map(newsToNewsDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
