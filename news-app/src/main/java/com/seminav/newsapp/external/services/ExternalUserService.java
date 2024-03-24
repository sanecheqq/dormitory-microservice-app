package com.seminav.newsapp.external.services;

import java.util.List;

public interface ExternalUserService {
    String getUserRoleFromUserAndValidateJWT(String authHeader);

    List<String> getUserSavedNews(String authHeader);

    void deleteSavedNewsFromFollowers(String newsId, String authHeader);
}
