package com.seminav.newsapp.external.services;

import com.seminav.newsapp.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

@Service
public abstract class DiscoveryClientService {
    @Autowired
    private DiscoveryClient discoveryClient;

    protected ServiceInstance getAvaliableServiceInstance(String serviceName) {
            return discoveryClient.getInstances(serviceName).stream()
                    .findFirst()
                    .orElseThrow(() -> new InstanceNotFoundException("Instance storage-app was not found"));
    }
}
