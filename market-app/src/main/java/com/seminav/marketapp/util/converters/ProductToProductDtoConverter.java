package com.seminav.marketapp.util.converters;

import com.seminav.marketapp.messages.dtos.ProductDto;
import com.seminav.marketapp.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ProductToProductDtoConverter implements Converter<Product, ProductDto> {
    private final ImageToFileDtoConverter imageToFileDtoConverter;
    @Override
    public ProductDto convert(Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getCategory()
                        .toString(),
                product.getDescription(),
                product.getPrice(),
                product.getDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")),
                product.getAddress(),
                product.getImages().stream()
                        .map(imageToFileDtoConverter::convert)
                        .toList()
        );
    }
}