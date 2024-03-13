package com.seminav.newsapp.util.converters;

import com.seminav.newsapp.external.messages.FileDto;
import com.seminav.newsapp.model.Image;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageToFileDtoConverter implements Converter<Image, FileDto> {
    @Override
    public FileDto convert(Image image) {
        return new FileDto(
                image.getImageId(),
                image.getImageName(),
                image.getUrl()
        );
    }
}
