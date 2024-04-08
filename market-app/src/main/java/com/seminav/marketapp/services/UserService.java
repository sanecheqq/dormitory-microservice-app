package com.seminav.marketapp.services;

import com.seminav.marketapp.messages.GetMyProductsResponse;
import com.seminav.marketapp.model.User;

public interface UserService {
    User getUserOrElseSave(String userId);

    GetMyProductsResponse getProducts(String userId);

    User save(User user);
}
