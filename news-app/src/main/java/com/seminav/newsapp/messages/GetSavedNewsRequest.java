package com.seminav.newsapp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetSavedNewsRequest(
    @JsonProperty(value = "ids_of_news", required = true)
    List<String> idsOfNews
) {}
