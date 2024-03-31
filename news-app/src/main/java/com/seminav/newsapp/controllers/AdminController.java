package com.seminav.newsapp.controllers;

import com.seminav.newsapp.exceptions.NotEnoughRootsException;
import com.seminav.newsapp.external.services.ExternalUserService;
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
    private final ExternalUserService externalUserService;

    @PostMapping("/news")
    public ResponseEntity<NewsDto> createNews(
            @ModelAttribute @Valid CreateNewsRequest createNewsRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        checkRoleOrElseThrow(externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader));
        var userDto = externalUserService.getUserDto(authorizationHeader);
        return ResponseEntity.ok(newsService.createNews(createNewsRequest, userDto.address()));
    }

    @DeleteMapping("news/{news_id}")
    public ResponseEntity<?> deleteNews(
            @PathVariable(name = "news_id") String newsId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        externalUserService.deleteSavedNewsFromFollowers(newsId, authorizationHeader);
        newsService.deleteNews(newsId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("news/{news_id}")
    public ResponseEntity<NewsDto> updateNews(
            @PathVariable(name = "news_id") String newsId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "content", required = false) String content,
            @ModelAttribute UpdateNewsRequest updateNewsRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        checkRoleOrElseThrow(externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader));
        NewsDto newsDto = newsService.updateNews(newsId, title, category, content, updateNewsRequest.images(), updateNewsRequest.documents());
        return ResponseEntity.ok(newsDto);
    }

    @PutMapping("news/{news_id}")
    public ResponseEntity<NewsDto> putNews(
            @PathVariable(name = "news_id") String newsId,
            @ModelAttribute @Valid CreateNewsRequest createNewsRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        checkRoleOrElseThrow(externalUserService.getUserRoleFromUserAndValidateJWT(authorizationHeader));
        NewsDto newsDto = newsService.putNews(newsId, createNewsRequest);
        return ResponseEntity.ok(newsDto);
    }


    private void checkRoleOrElseThrow(String userRole) {
        if (userRole.equals("USER")) {
            throw new NotEnoughRootsException("Min required role is ADMIN. Your role is " + userRole);
        }
    }

}
