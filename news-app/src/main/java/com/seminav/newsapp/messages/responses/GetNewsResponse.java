package com.seminav.newsapp.messages.responses;

import com.seminav.newsapp.messages.dtos.NewsDtoWithFavoriteField;

import java.util.List;

public record GetNewsResponse(
        List<NewsDtoWithFavoriteField> news
) {}
