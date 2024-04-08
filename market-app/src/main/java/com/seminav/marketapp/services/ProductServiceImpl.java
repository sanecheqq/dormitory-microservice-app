package com.seminav.marketapp.services;

import com.seminav.marketapp.external.messages.FileDto;
import com.seminav.marketapp.external.services.CloudStorageService;
import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.GetProductsForValidationResponse;
import com.seminav.marketapp.messages.GetProductsResponse;
import com.seminav.marketapp.messages.dtos.ProductDto;
import com.seminav.marketapp.model.Image;
import com.seminav.marketapp.model.Product;
import com.seminav.marketapp.model.ProductCategory;
import com.seminav.marketapp.model.ProductStatus;
import com.seminav.marketapp.repositories.ProductRepository;
import com.seminav.marketapp.util.converters.FileDtoToImageConverter;
import com.seminav.marketapp.util.converters.ProductToProductDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CloudStorageService cloudStorageService;
    private final ProductToProductDtoConverter productToProductDtoConverter;
    private final FileDtoToImageConverter fileDtoToImageConverter;
    private final HibernateSearchService hibernateSearchService;
    private final ExecutorService sendingRequestsExecutor = Executors.newFixedThreadPool(15);

    @Override
    public ProductDto createProduct(CreateProductRequest createProductRequest) {
        List<MultipartFile> images = createProductRequest.images();
        CompletableFuture<List<FileDto>> uploadImagesFuture = uploadFilesAsync(images);

        List<FileDto> imageFileDtos = new ArrayList<>();
        try {
            imageFileDtos = uploadImagesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Troubles with getting result from CompletableFuture\n" + e.getMessage());
        }

        Product product = new Product();
        product.setProductName(createProductRequest.name());
        product.setCategory(ProductCategory.valueOf(createProductRequest.category()));
        product.setDescription(createProductRequest.description());
        product.setPrice(BigDecimal.valueOf(createProductRequest.price()));
        product.setDate(Timestamp.from(Instant.now()));
        product.setAddress(createProductRequest.address());
        product.setStatus(ProductStatus.VALIDATING);
        product.addAllImages(imageFileDtos.stream()
                .map(fileDtoToImageConverter::convert)
                .toList()
        );
        return productToProductDtoConverter.convert(productRepository.save(product));
    }

    @Override
    public void changeProductStatus(String productId, String status) {
        Product product = findByIdOrElseThrow(productId);
        product.setStatus(ProductStatus.valueOf(status));
        productRepository.save(product);
    }

    @Override
    public GetProductsForValidationResponse getProductsForValidation() {
        List<Product> productsForValidation = productRepository.findAllByStatus(ProductStatus.VALIDATING);
        return new GetProductsForValidationResponse(productsForValidation.stream()
                .map(productToProductDtoConverter::convert)
                .toList()
        );
    }

    @Override
    public void archiveProduct(String id) {
        Product product = findByIdOrElseThrow(id);
        product.setStatus(ProductStatus.ARCHIVED);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(String id) {
        List<String> imageIds = findByIdOrElseThrow(id).getImages().stream().map(Image::getImageId).toList();
        cloudStorageService.deleteFiles(imageIds);
        productRepository.deleteById(id);
    }

    @Override
    public GetProductsResponse getProducts(ProductCategory category, Double minPrice, Double maxPrice, String searchPattern) {
        List<Product> result;
        if (category == null && searchPattern == null) {
            result  = hibernateSearchService.searchForProducts(minPrice, maxPrice);
        } else if (category == null) {
            result  = hibernateSearchService.searchForProducts(searchPattern, minPrice, maxPrice);
        } else if (searchPattern == null) {
            result  = hibernateSearchService.searchForProducts(category, minPrice, maxPrice);
        } else {
            result = hibernateSearchService.searchForProducts(searchPattern, category, minPrice, maxPrice);
        }
        return new GetProductsResponse(
                result.stream()
                        .map(productToProductDtoConverter::convert)
                        .toList()
        );
    }

    @Override
    public ProductDto getProductById(String productId) {
        return productToProductDtoConverter.convert(findByIdOrElseThrow(productId));
    }

    private Product findByIdOrElseThrow(String productId) {
        return productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
    }

    private CompletableFuture<List<FileDto>> uploadFilesAsync(List<MultipartFile> images) {
        return CompletableFuture.supplyAsync(
                () -> cloudStorageService.uploadFiles(images),
                sendingRequestsExecutor
        ).handle((result, ex) -> {
            if (ex != null)
                return new ArrayList<>();
            else
                return result;
        });
    }
}
