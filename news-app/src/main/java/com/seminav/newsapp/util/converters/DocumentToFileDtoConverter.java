package com.seminav.newsapp.util.converters;

import com.seminav.newsapp.external.messages.FileDto;
import com.seminav.newsapp.model.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DocumentToFileDtoConverter implements Converter<Document, FileDto> {

    @Override
    public FileDto convert(Document doc) {
        return new FileDto(
                doc.getDocumentId(),
                doc.getFileName(),
                doc.getUrl()
        );
    }
}
