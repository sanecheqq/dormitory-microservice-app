package com.seminav.marketapp.external.services;


import com.seminav.marketapp.external.messages.UserDto;

import java.util.List;

public interface ExternalUserService {
    String getUserRoleFromUserAndValidateJWT(String authHeader);

    List<String> getUserSavedProducts(String authHeader);

    UserDto getUserDto(String authHeader);

    void deleteSavedProductsFromFollowers(String newsId, String authHeader);
}
