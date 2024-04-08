package com.seminav.marketapp.services;

import com.seminav.marketapp.exceptions.UserNotFoundException;
import com.seminav.marketapp.messages.GetProductsResponse;
import com.seminav.marketapp.model.User;
import com.seminav.marketapp.repositories.UserRepository;
import com.seminav.marketapp.util.converters.ProductToProductDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProductToProductDtoConverter productToProductDtoConverter;
    @Override
    public User getUserOrElseSave(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setUserId(userId);
            return userRepository.save(newUser);
        }
        return user.get();
    }

    @Override
    public GetProductsResponse getProducts(String userId) {
        return new GetProductsResponse(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId))
                        .getProducts().stream()
                        .map(productToProductDtoConverter::convert)
                        .toList()
        );
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
