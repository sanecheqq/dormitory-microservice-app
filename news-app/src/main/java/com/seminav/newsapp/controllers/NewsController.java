package com.seminav.newsapp.controllers;

import com.seminav.newsapp.messages.GetNewsResponse;
import com.seminav.newsapp.messages.SortType;
import com.seminav.newsapp.model.NewsCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    @GetMapping()
    public ResponseEntity<GetNewsResponse> getNews(
            @RequestParam(name = "search_pattern", required = false) String searchPattern,
            @RequestParam(name = "category", required = false) NewsCategory newsCategory,
            @RequestParam(name = "sort_type", defaultValue = "DESCENDING") SortType sortType
    ) {

        return ResponseEntity.ok(new GetNewsResponse(new ArrayList<>()));
    }
}
