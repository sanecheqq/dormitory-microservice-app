package com.seminav.newsapp.controllers;

import com.seminav.newsapp.messages.GetNewsResponse;
import com.seminav.newsapp.messages.GetSavedNewsRequest;
import com.seminav.newsapp.messages.GetSavedNewsResponse;
import com.seminav.newsapp.messages.SortType;
import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.model.NewsCategory;
import com.seminav.newsapp.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping()
    public ResponseEntity<GetNewsResponse> getNews(
            @RequestParam(name = "category", required = false) NewsCategory newsCategory,
            @RequestParam(name = "search_pattern", required = false) String searchPattern,
            @RequestParam(name = "sort_type", defaultValue = "DESCENDING") SortType sortType
    ) {
        var news = newsService.getNews(newsCategory, searchPattern, sortType);
        return ResponseEntity.ok(new GetNewsResponse(news));
    }

    @GetMapping("/saved")
    public ResponseEntity<GetSavedNewsResponse> getSavedNews(
            @RequestBody GetSavedNewsRequest getSavedNewsRequest
    ) {
        List<NewsDto> savedNews = newsService.getSavedNews(getSavedNewsRequest.idsOfNews());
        return ResponseEntity.ok(new GetSavedNewsResponse(savedNews));
    }
}
