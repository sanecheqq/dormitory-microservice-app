package com.seminav.newsapp.model;

import lombok.Getter;

@Getter
public enum NewsCategory {
    ORDERS("Приказы"),
    NEWS("Новости");

    private final String category;

    NewsCategory(String category) {
        this.category = category;
    }
}
