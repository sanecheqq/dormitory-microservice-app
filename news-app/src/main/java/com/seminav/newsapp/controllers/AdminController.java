package com.seminav.newsapp.controllers;

import com.seminav.newsapp.messages.CreateNewsRequest;
import com.seminav.newsapp.messages.UpdateNewsRequest;
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
    //TODO: в будущем нужно будет отправлять запрос в юзера на проверку роли/авторизации, чтобы левые типочки не могли заходить на сайт
    // ХЫЗЫ.
    @PostMapping
    public ResponseEntity<NewsDto> createNews(
            @ModelAttribute @Valid CreateNewsRequest createNewsRequest
    ) {
        return ResponseEntity.ok(newsService.createNews(createNewsRequest));
    }

    @DeleteMapping("/{news_id}")
    public ResponseEntity<?> deleteNews(
            @PathVariable(name = "news_id") String newsId
    ) {
        newsService.deleteNews(newsId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{news_id}")
    public ResponseEntity<NewsDto> updateNews(
            @PathVariable(name = "news_id") String newsId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "content", required = false) String content,
            @ModelAttribute UpdateNewsRequest updateNewsRequest
    ) {
        NewsDto newsDto = newsService.updateNews(newsId, title, category, content, updateNewsRequest.images(), updateNewsRequest.documents());
        return ResponseEntity.ok(newsDto);
    }


}
