package com.seminav.marketapp.util.converters;

import com.seminav.marketapp.external.messages.FileDto;
import com.seminav.marketapp.model.Image;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FileDtoToImageConverter implements Converter<FileDto, Image> {
    @Override
    public Image convert(FileDto fileDto) {
        Image image = new Image();
        image.setImageId(fileDto.fileId());
        image.setUrl(fileDto.url());
        return image;
    }
}
