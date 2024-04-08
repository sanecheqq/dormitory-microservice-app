package com.seminav.marketapp.services;

import com.seminav.marketapp.messages.GetProductsResponse;
import com.seminav.marketapp.model.User;

public interface UserService {
    User getUserOrElseSave(String userId);

    GetProductsResponse getProducts(String userId);

    User save(User user);
}
