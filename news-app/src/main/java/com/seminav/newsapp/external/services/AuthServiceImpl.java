package com.seminav.newsapp.external.services;

import com.seminav.newsapp.util.HeaderRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl extends DiscoveryClientService implements AuthService {
    private final RestTemplate restTemplate;

    public AuthServiceImpl(RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }
    @Override
    public String getUserRoleFromUserAndValidateJWT(String authHeader) {
        var storageInstance = getAvaliableServiceInstance("user-app");
        URI userUri = storageInstance.getUri().resolve("/authenticate");
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", authHeader));
        restTemplate.setInterceptors(interceptors);
        return restTemplate.getForObject(userUri, String.class);
    }
}
