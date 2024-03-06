package com.seminav.newsapp.external.messages;

import java.util.List;

public record DeleteFilesRequest(
        List<String> ids
) {
}
