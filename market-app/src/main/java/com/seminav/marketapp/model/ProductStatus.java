package com.seminav.marketapp.model;

import lombok.Getter;

@Getter
public enum ProductStatus {
    VALIDATING("На проверке"),
    REJECTED("Требует изменений"),
    PUBLISHED("Опубликован"),
    ARCHIVED("Снят с публикации");

    private final String statusName;

    ProductStatus(String name) {
        this.statusName = name;
    }
}
