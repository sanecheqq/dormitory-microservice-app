package com.seminav.newsapp.util.converters;

import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.model.News;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NewsToNewsDtoConverter implements Converter<News, NewsDto> {
    @Override
    public NewsDto convert(News news) {
        return new NewsDto(
                news.getId(),
                news.getTitle(),
                news.getCategory()
                        .toString(),
                news.getContent(),
                news.getImageId(),
                news.getDate()
                        .toString()
        );
    }
}
