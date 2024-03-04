package com.seminav.newsapp.util.converters;

import com.seminav.newsapp.external.messages.FileDto;
import com.seminav.newsapp.model.Image;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FileDtoToImageConverter implements Converter<FileDto, Image> {
    @Override
    public Image convert(FileDto fileDto) {
        return new Image(fileDto.fileId(), fileDto.url());
    }
}
