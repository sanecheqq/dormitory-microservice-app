package com.seminav.newsapp.util.converters;

import com.seminav.newsapp.external.messages.FileDto;
import com.seminav.newsapp.model.Image;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FileDtoToImageConverter implements Converter<FileDto, Image> {
    @Override
    public Image convert(FileDto fileDto) {
        Image image = new Image();
        image.setImageId(fileDto.fileId());
        image.setImageName(fileDto.fileName());
        image.setUrl(fileDto.url());
        return image;
    }
}
