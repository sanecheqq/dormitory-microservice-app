package com.seminav.marketapp.services;

import com.seminav.marketapp.external.messages.UserDto;
import com.seminav.marketapp.messages.GetMyProductsResponse;
import com.seminav.marketapp.model.User;

public interface UserService {
    User getUserOrElseSave(UserDto userDto);

    GetMyProductsResponse getProducts(String userId);

    User save(User user);
}
