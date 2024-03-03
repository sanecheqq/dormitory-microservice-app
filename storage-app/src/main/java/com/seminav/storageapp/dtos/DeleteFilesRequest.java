package com.seminav.storageapp.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DeleteFilesRequest(
        @NotEmpty
        List<String> ids
) {}
