package com.seminav.marketapp.services;

import com.seminav.marketapp.external.messages.FileDto;
import com.seminav.marketapp.external.messages.UserDto;
import com.seminav.marketapp.external.services.CloudStorageService;
import com.seminav.marketapp.messages.CreateProductRequest;
import com.seminav.marketapp.messages.GetProductsForValidationResponse;
import com.seminav.marketapp.messages.GetProductsResponse;
import com.seminav.marketapp.messages.PutProductRequest;
import com.seminav.marketapp.messages.dtos.ProductDto;
import com.seminav.marketapp.messages.dtos.ProductDtoWithFavoriteField;
import com.seminav.marketapp.model.*;
import com.seminav.marketapp.repositories.ProductRepository;
import com.seminav.marketapp.util.converters.FileDtoToImageConverter;
import com.seminav.marketapp.util.converters.ImageToFileDtoConverter;
import com.seminav.marketapp.util.converters.ProductToProductDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final ImageToFileDtoConverter imageToFileDtoConverter;
    private final FileDtoToImageConverter fileDtoToImageConverter;
    private final HibernateSearchService hibernateSearchService;
    private final UserService userService;
    private final ExecutorService sendingRequestsExecutor = Executors.newFixedThreadPool(15);

    @Override
    public ProductDto createProduct(CreateProductRequest createProductRequest, UserDto userDto) {
        List<MultipartFile> images = createProductRequest.images();
        CompletableFuture<List<FileDto>> uploadImagesFuture = uploadFilesAsync(images);

        List<FileDto> imageFileDtos = new ArrayList<>();
        try {
            imageFileDtos = uploadImagesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Troubles with getting result from CompletableFuture\n" + e.getMessage());
        }

        User user = userService.getUserOrElseSave(userDto);

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
        product.setUser(user);
        product = productRepository.save(product);
        user.addProduct(product);
        userService.save(user);

        return productToProductDtoConverter.convert(product);
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
        try {
            List<String> imageIds = findByIdOrElseThrow(id).getImages().stream().map(Image::getImageId).toList();
            cloudStorageService.deleteFiles(imageIds);
            productRepository.deleteById(id);
            //todo: send request to user-app to delete from followers
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
    }

    @Override
    public GetProductsResponse getProducts(ProductCategory category, Double minPrice, Double maxPrice, String searchPattern, Integer page, Set<String> savedIds) {
        List<Product> result;
        if (category == null && (searchPattern == null || searchPattern.isBlank())) {
            result  = hibernateSearchService.searchForProducts(minPrice, maxPrice, page);
        } else if (category == null) {
            result  = hibernateSearchService.searchForProducts(searchPattern, minPrice, maxPrice, page);
        } else if (searchPattern == null || searchPattern.isBlank()) {
            result  = hibernateSearchService.searchForProducts(category, minPrice, maxPrice, page);
        } else {
            result = hibernateSearchService.searchForProducts(searchPattern, category, minPrice, maxPrice, page);
        }
        return new GetProductsResponse(
                convertProductsToProductDtoWithFavoriteFieldList(result, savedIds)
        );
    }

    private List<ProductDtoWithFavoriteField> convertProductsToProductDtoWithFavoriteFieldList(List<Product> products, Set<String> savedProductsIds) {
        List<ProductDtoWithFavoriteField> res = new ArrayList<>();
        for (var product : products) {
            res.add(new ProductDtoWithFavoriteField(
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory().toString(),
                    product.getDescription(),
                    product.getPrice().doubleValue(),
                    product.getDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")),
                    product.getAddress(),
                    product.getUser().getTgUsername(),
                    product.getImages().stream().map(imageToFileDtoConverter::convert).toList(),
                    savedProductsIds.contains(product.getProductId())
            ));
        }
        return res;
    }

    @Override
    public ProductDto getProductById(String productId) {
        return productToProductDtoConverter.convert(findByIdOrElseThrow(productId));
    }

    @Override
    public void approveAllProducts() {
        List<Product> validatingProducts = productRepository.findAllByStatus(ProductStatus.VALIDATING);
        for (var product : validatingProducts) {
            product.setStatus(ProductStatus.PUBLISHED);
        }
        productRepository.saveAll(validatingProducts);
    }

    @Override
    public GetProductsResponse getFavoriteProducts(List<String> savedProducts) {
        List<Product> favoriteProducts = productRepository.findAllById(savedProducts);
        return new GetProductsResponse(
                convertProductsToProductDtoWithFavoriteFieldList(favoriteProducts, new HashSet<>(savedProducts))
        );
    }

    @Override
    public ProductDto putProduct(PutProductRequest putProductRequest, String id) {
        Product product = findByIdOrElseThrow(id);
        List<Image> productImages = product.getImages();

        Set<String> oldImagesToSave = new HashSet<>(putProductRequest.oldImages());
        List<Image> imagesToRemove = productImages.stream()
                .filter(v -> !oldImagesToSave.contains((v.getImageId())))
                .toList();
        imagesToRemove.forEach(product::removeImage);
        cloudStorageService.deleteFiles(imagesToRemove.stream()
                .map(Image::getImageId)
                .toList()
        );
        List<FileDto> savedNewImagesDtos = new ArrayList<>();
        try {
            savedNewImagesDtos = uploadFilesAsync(putProductRequest.newImages()).get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Troubles with getting result from CompletableFuture\n" + e.getMessage());
        }
        product.addAllImages(savedNewImagesDtos.stream()
                .map(fileDtoToImageConverter::convert)
                .toList()
        );

        return productToProductDtoConverter.convert(productRepository.save(product));
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
