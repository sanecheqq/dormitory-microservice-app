package com.seminav.newsapp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record GetSavedNewsRequest(
    @JsonProperty(value = "ids_of_news", required = true)
    @NotEmpty
    List<String> idsOfNews
) {}
