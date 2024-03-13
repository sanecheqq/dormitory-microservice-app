package com.seminav.newsapp.controllers;

import com.seminav.newsapp.external.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PingController {
    private final AuthService authService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/ping-user-app")
    public String pingUserApp(
            @RequestHeader("Authorization") String authorizationHeader

    ) {
        return authService.getUserRoleFromUserAndValidateJWT(authorizationHeader);
    }
}
