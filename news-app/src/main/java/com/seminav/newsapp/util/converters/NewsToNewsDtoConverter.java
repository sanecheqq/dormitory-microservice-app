package com.seminav.newsapp.util.converters;

import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.model.News;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class NewsToNewsDtoConverter implements Converter<News, NewsDto> {
    private final ImageToFileDtoConverter imageToFileDtoConverter;
    private final DocumentToFileDtoConverter documentToFileDtoConverter;
    @Override
    public NewsDto convert(News news) {
        return new NewsDto(
                news.getNewsid(),
                news.getTitle(),
                news.getCategory()
                        .toString(),
                news.getContent(),
                news.getImages().stream()
                        .map(imageToFileDtoConverter::convert)
                        .toList(),
                news.getDocuments().stream()
                        .map(documentToFileDtoConverter::convert)
                        .toList(),
                news.getDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy"))
        );
    }
}
