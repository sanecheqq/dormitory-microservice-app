package com.seminav.marketapp.services;

import com.seminav.marketapp.exceptions.UserNotFoundException;
import com.seminav.marketapp.external.messages.UserDto;
import com.seminav.marketapp.messages.GetMyProductsResponse;
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
    public User getUserOrElseSave(UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.id());
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setUserId(userDto.id());
            newUser.setTgUsername(userDto.tgUsername());
            return userRepository.save(newUser);
        }
        return user.get();
    }

    @Override
    public GetMyProductsResponse getProducts(String userId) {
        return new GetMyProductsResponse(
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
