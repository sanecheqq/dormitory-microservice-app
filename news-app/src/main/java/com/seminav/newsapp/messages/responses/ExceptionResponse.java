package com.seminav.newsapp.messages.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionResponse(
        @JsonProperty("error_message")
        String errorMessage
) {
}
