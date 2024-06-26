package com.seminav.newsapp.external.services;

import com.seminav.newsapp.exceptions.DeleteSavedNewsFromFollowersException;
import com.seminav.newsapp.external.messages.DeleteNewsFromFollowersRequest;
import com.seminav.newsapp.external.messages.GetUserResponse;
import com.seminav.newsapp.external.messages.UserDto;
import com.seminav.newsapp.util.HeaderRequestInterceptor;
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
    public void deleteSavedNewsFromFollowers(String newsId, String authHeader) {
        var storageInstance = getAvaliableServiceInstance("user-app");
        URI deleteSavedNewsFromFollowersUri = storageInstance.getUri().resolve("/saved-news/followers");
        restTemplate.setInterceptors(buildAuthHeaderInterceptorList(authHeader));
        HttpEntity<DeleteNewsFromFollowersRequest> request = new HttpEntity<>(new DeleteNewsFromFollowersRequest(newsId));
        ResponseEntity<String> response = restTemplate.exchange(deleteSavedNewsFromFollowersUri, HttpMethod.DELETE, request, String.class);
        if (response.getStatusCode().is4xxClientError()) {
            throw new DeleteSavedNewsFromFollowersException("News " + newsId + " was not deleted from followers");
        } else {
            System.out.println("News " + newsId + " was deleted!");
        }
    }

    public List<String> getUserSavedNews(String authHeader) {
        var storageInstance = getAvaliableServiceInstance("user-app");
        URI userUri = storageInstance.getUri().resolve("/saved-news");
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
