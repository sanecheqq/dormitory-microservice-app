package com.seminav.newsapp.controllers;

import com.seminav.newsapp.exceptions.NotEnoughRootsException;
import com.seminav.newsapp.external.services.AuthService;
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
    private final AuthService authService;
    //TODO: в будущем нужно будет отправлять запрос в юзера на проверку роли/авторизации, чтобы левые типочки не могли заходить на сайт
    // ХЫЗЫ.
    @PostMapping("/news")
    public ResponseEntity<NewsDto> createNews(
            @ModelAttribute @Valid CreateNewsRequest createNewsRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        checkRoleOrElseThrow(authService.getUserRoleFromUserAndValidateJWT(authorizationHeader));
        return ResponseEntity.ok(newsService.createNews(createNewsRequest));
    }

    @DeleteMapping("news/{news_id}")
    public ResponseEntity<?> deleteNews(
            @PathVariable(name = "news_id") String newsId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        checkRoleOrElseThrow(authService.getUserRoleFromUserAndValidateJWT(authorizationHeader));
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
        checkRoleOrElseThrow(authService.getUserRoleFromUserAndValidateJWT(authorizationHeader));
        NewsDto newsDto = newsService.updateNews(newsId, title, category, content, updateNewsRequest.images(), updateNewsRequest.documents());
        return ResponseEntity.ok(newsDto);
    }


    private void checkRoleOrElseThrow(String userRole) {
        if (userRole.equals("USER")) {
            throw new NotEnoughRootsException("Min required role is ADMIN. Your role is " + userRole);
        }
    }

}
