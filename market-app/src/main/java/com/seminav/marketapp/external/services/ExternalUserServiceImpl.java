package com.seminav.marketapp.external.services;

import com.seminav.marketapp.exceptions.DeleteSavedProductFromFollowersException;
import com.seminav.marketapp.external.messages.DeleteProductFromFollowersRequest;
import com.seminav.marketapp.external.messages.GetUserResponse;
import com.seminav.marketapp.external.messages.UserDto;
import com.seminav.marketapp.util.HeaderRequestInterceptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ExternalUserServiceImpl extends DiscoveryClientService implements ExternalUserService {
    private final RestTemplate restTemplate;

    public ExternalUserServiceImpl(RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }
    @Override
    public String getUserRoleFromUserAndValidateJWT(String authHeader) {
        var storageInstance = getAvaliableServiceInstance("user-app");
        URI userUri = storageInstance.getUri().resolve("/authenticate");
        restTemplate.setInterceptors(buildAuthHeaderInterceptorList(authHeader));
        return restTemplate.getForObject(userUri, String.class);
    }

    @Override
    public void deleteSavedProductsFromFollowers(String productId, String authHeader) {
        var storageInstance = getAvaliableServiceInstance("user-app");
        URI deleteSavedNewsFromFollowersUri = storageInstance.getUri().resolve("/saved-products/followers"); // todo: ждем ручки на юзере
        restTemplate.setInterceptors(buildAuthHeaderInterceptorList(authHeader));
        HttpEntity<DeleteProductFromFollowersRequest> request = new HttpEntity<>(new DeleteProductFromFollowersRequest(productId));
        ResponseEntity<String> response = restTemplate.exchange(deleteSavedNewsFromFollowersUri, HttpMethod.DELETE, request, String.class);
        if (response.getStatusCode().is4xxClientError()) {
            throw new DeleteSavedProductFromFollowersException("Product " + productId + " was not deleted from followers");
        } else {
            System.out.println("Product " + productId + " was deleted!");
        }
    }

    public List<String> getUserSavedProducts(String authHeader) {
        var storageInstance = getAvaliableServiceInstance("user-app");
        URI userUri = storageInstance.getUri().resolve("/saved-products"); // todo: ждем ручки на юзере
        restTemplate.setInterceptors(buildAuthHeaderInterceptorList(authHeader));
        String[] savedNewsIds = restTemplate.getForObject(userUri, String[].class);
        if (savedNewsIds == null || savedNewsIds.length == 0) {
            return new ArrayList<>();
        } else {
            return List.of(savedNewsIds);

        }
    }

    public UserDto getUserDto(String authHeader) {
        var storageInstance = getAvaliableServiceInstance("user-app");
        URI userUri = storageInstance.getUri().resolve("/user");
        restTemplate.setInterceptors(buildAuthHeaderInterceptorList(authHeader));
        return Objects.requireNonNull(restTemplate.getForObject(userUri, GetUserResponse.class)).userDTO();
    }

    private static List<ClientHttpRequestInterceptor> buildAuthHeaderInterceptorList(String authHeader) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", authHeader));
        return interceptors;
    }


}
