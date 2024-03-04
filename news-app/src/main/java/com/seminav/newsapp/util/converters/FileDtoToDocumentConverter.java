package com.seminav.newsapp.util.converters;

import com.seminav.newsapp.external.messages.FileDto;
import com.seminav.newsapp.model.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FileDtoToDocumentConverter implements Converter<FileDto, Document> {
    @Override
    public Document convert(FileDto fileDto) {
        return new Document(fileDto.fileId(), fileDto.url());
    }
}
