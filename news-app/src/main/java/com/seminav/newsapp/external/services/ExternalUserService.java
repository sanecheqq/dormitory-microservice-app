package com.seminav.newsapp.external.services;

import com.seminav.newsapp.external.messages.UserDto;

import java.util.List;

public interface ExternalUserService {
    String getUserRoleFromUserAndValidateJWT(String authHeader);

    List<String> getUserSavedNews(String authHeader);

    UserDto getUserDto(String authHeader);

    void deleteSavedNewsFromFollowers(String newsId, String authHeader);
}
