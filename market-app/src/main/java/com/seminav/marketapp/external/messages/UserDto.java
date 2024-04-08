package com.seminav.marketapp.external.messages;

public record UserDto(
        String id,
        String username,
        String name,
        String role,
        String surname,
        String patronymic,
        String email,
        String phoneNumber,
        String tgUsername,
        String address,
        String password,
        String salt
) {}
