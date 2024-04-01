package com.seminav.marketapp.external.messages;

import java.util.List;

public record DeleteFilesRequest(
        List<String> ids
) {}