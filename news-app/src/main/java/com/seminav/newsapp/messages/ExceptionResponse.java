package com.seminav.newsapp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionResponse(
        @JsonProperty("error_message")
        String errorMessage
) {
}
