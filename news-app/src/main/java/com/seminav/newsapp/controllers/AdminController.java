package com.seminav.newsapp.controllers;

import com.seminav.newsapp.messages.CreateNewsRequest;
import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.services.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<NewsDto> createNews(
            @ModelAttribute @Valid CreateNewsRequest createNewsRequest
    ) {
        return ResponseEntity.ok(newsService.createNews(createNewsRequest));
    }

}
