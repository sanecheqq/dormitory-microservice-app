package com.seminav.newsapp.external.services;

import com.seminav.newsapp.util.HeaderRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

    private static List<ClientHttpRequestInterceptor> buildAuthHeaderInterceptorList(String authHeader) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", authHeader));
        return interceptors;
    }


}
