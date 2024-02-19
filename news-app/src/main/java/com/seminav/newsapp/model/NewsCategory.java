package com.seminav.newsapp.model;

public enum NewsCategory {
    ORDERS("Приказы"),
    CATEGORY2("Забыл");

    private final String category;

    NewsCategory(String category) {
        this.category = category;
    }
}
