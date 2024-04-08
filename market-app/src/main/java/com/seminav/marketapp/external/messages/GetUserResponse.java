package com.seminav.marketapp.external.messages;

public record GetUserResponse(
        UserDto userDTO,
        CertificateDto fluoroCertificateDTO,
        CertificateDto stdsCertificateDTO
) {}
