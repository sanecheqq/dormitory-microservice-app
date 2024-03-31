package com.seminav.newsapp.controllers;

import com.seminav.newsapp.external.services.ExternalUserService;
import com.seminav.newsapp.messages.responses.GetNewsResponse;
import com.seminav.newsapp.messages.responses.GetSavedNewsResponse;
import com.seminav.newsapp.messages.responses.SortType;
import com.seminav.newsapp.model.NewsCategory;
import com.seminav.newsapp.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final ExternalUserService externalUserService;

    @GetMapping()
    public ResponseEntity<GetNewsResponse> getNews(
            @RequestParam(name = "category", required = false) NewsCategory newsCategory,
            @RequestParam(name = "search_pattern", defaultValue = "null") String searchPattern,
            @RequestParam(name = "sort_type", defaultValue = "DESCENDING") SortType sortType,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        var savedNewsIdsList = externalUserService.getUserSavedNews(authorizationHeader);
        var userDto = externalUserService.getUserDto(authorizationHeader);
        var news = newsService.getNews(newsCategory, searchPattern, sortType, savedNewsIdsList, userDto.address());
        return ResponseEntity.ok(new GetNewsResponse(news));
    }

    @GetMapping("/favorites")
    public ResponseEntity<GetSavedNewsResponse> getSavedNews(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        var savedNewsIdsList = externalUserService.getUserSavedNews(authorizationHeader);
        var savedNews = newsService.getSavedNews(savedNewsIdsList);
        return ResponseEntity.ok(new GetSavedNewsResponse(savedNews));
    }
}
