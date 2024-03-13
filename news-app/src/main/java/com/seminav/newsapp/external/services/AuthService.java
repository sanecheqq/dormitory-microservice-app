package com.seminav.newsapp.external.services;

public interface AuthService {
    String getUserRoleFromUserAndValidateJWT(String authHeader);
}
