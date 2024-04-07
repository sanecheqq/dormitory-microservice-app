package com.seminav.newsapp.external.messages;

public record GetUserResponse(
        UserDto userDTO,
        CertificateDto fluoroCertificateDTO,
        CertificateDto stdsCertificateDTO
) {}
