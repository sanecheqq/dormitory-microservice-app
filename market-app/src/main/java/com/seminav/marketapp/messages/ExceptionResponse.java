package com.seminav.marketapp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionResponse(
        @JsonProperty("error_message")
        String errorMessage
) {
}
