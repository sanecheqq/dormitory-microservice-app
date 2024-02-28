package com.seminav.newsapp.messages;

import lombok.Getter;

@Getter
public enum SortType {
    DESCENDING("descending"),
    ASCENDING("ascending");

    private final String sortType;

    SortType(String sortType) {
        this.sortType = sortType;
    }
}
