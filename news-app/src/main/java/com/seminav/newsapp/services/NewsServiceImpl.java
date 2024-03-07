package com.seminav.newsapp.services;

import com.seminav.newsapp.exceptions.NewsNotFoundException;
import com.seminav.newsapp.external.messages.FileDto;
import com.seminav.newsapp.external.services.CloudStorageService;
import com.seminav.newsapp.messages.CreateNewsRequest;
import com.seminav.newsapp.messages.SortType;
import com.seminav.newsapp.messages.dtos.NewsDto;
import com.seminav.newsapp.model.Document;
import com.seminav.newsapp.model.Image;
import com.seminav.newsapp.model.News;
import com.seminav.newsapp.model.NewsCategory;
import com.seminav.newsapp.repositories.NewsRepository;
import com.seminav.newsapp.util.converters.FileDtoToDocumentConverter;
import com.seminav.newsapp.util.converters.FileDtoToImageConverter;
import com.seminav.newsapp.util.converters.NewsToNewsDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepo;
    private final NewsToNewsDtoConverter newsToNewsDtoConverter;
    private final FileDtoToImageConverter fileDtoToImageConverter;
    private final FileDtoToDocumentConverter fileDtoToDocumentConverter;
    private final CloudStorageService cloudStorageService;

    private final ExecutorService sendingRequestsExecutor = Executors.newFixedThreadPool(15);

    @Override
    public List<NewsDto> getNews(NewsCategory newsCategory, String searchPattern, SortType sortType) {
        var sortOrder = sortType.equals(SortType.ASCENDING) ? Sort.Order.asc("date") : Sort.Order.desc("date");
        List<News> news = newsRepo.getNewsByNewsCategoryAndSearchPatternAndSortByDate(newsCategory, searchPattern, Sort.by(sortOrder));
        return convertListNewsToListNewsDto(news);
    }

    @Override
    public List<NewsDto> getSavedNews(List<String> idsOfNews) {
        List<News> savedNews = newsRepo.findAllById(idsOfNews);
        return convertListNewsToListNewsDto(savedNews);
    }

    @Override
    public NewsDto createNews(CreateNewsRequest createNewsRequest) {
        List<MultipartFile> images = createNewsRequest.images();
        List<MultipartFile> documents = createNewsRequest.documents();
        CompletableFuture<List<FileDto>> uploadImagesFuture = uploadFilesAsync(images);
        CompletableFuture<List<FileDto>> uploadDocumentsFuture = uploadFilesAsync(documents);

        List<FileDto> imageFileDtos = new ArrayList<>();
        List<FileDto> documentFileDtos = new ArrayList<>();
        try {
            imageFileDtos = uploadImagesFuture.get();
            documentFileDtos = uploadDocumentsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Troubles with getting result from CompletableFuture\n" + e.getMessage());
        }

        News news = new News();
        news.setTitle(createNewsRequest.title());
        news.setCategory(NewsCategory.valueOf(createNewsRequest.category()));
        news.setContent(createNewsRequest.content());
        news.setDate(Timestamp.from(Instant.now()));
        news.addAllImages(imageFileDtos.stream()
                .map(fileDtoToImageConverter::convert)
                .toList()
        );
        news.addAllDocuments(documentFileDtos.stream()
                .map(fileDtoToDocumentConverter::convert)
                .toList()
        );
        return newsToNewsDtoConverter.convert(newsRepo.save(news));
    }

    @Override
    public void deleteNews(String newsId) {
        News news = getNewsById(newsId);

        List<String> fileIds = new ArrayList<>();
        fileIds.addAll(news.getImages().stream()
                .map(Image::getImageId)
                .toList()
        );
        fileIds.addAll(news.getDocuments().stream()
                .map(Document::getDocumentId)
                .toList()
        );

        cloudStorageService.deleteFiles(fileIds);
        newsRepo.delete(news);
    }

    @Override
    public NewsDto updateNews(String newsId, String title, String category, String content, List<MultipartFile> images, List<MultipartFile> documents) {
        News news = getNewsById(newsId);
        if (title != null) {
            news.setTitle(title);
        }
        if (category != null) {
            news.setCategory(NewsCategory.valueOf(category));
        }
        if (content != null) {
            news.setContent(content);
        }
        CompletableFuture<List<FileDto>> uploadImagesFuture = uploadFilesAsync(images);
        CompletableFuture<List<FileDto>> uploadDocumentsFuture = uploadFilesAsync(documents);

        List<FileDto> imageFileDtos = new ArrayList<>();
        List<FileDto> documentFileDtos = new ArrayList<>();
        try {
            imageFileDtos = uploadImagesFuture.get();
            documentFileDtos = uploadDocumentsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Troubles with getting result from CompletableFuture\n" + e.getMessage());
        }

        news.addAllImages(imageFileDtos.stream()
                .map(fileDtoToImageConverter::convert)
                .toList()
        );

        news.addAllDocuments(documentFileDtos.stream()
                .map(fileDtoToDocumentConverter::convert)
                .toList()
        );

        return newsToNewsDtoConverter.convert(newsRepo.save(news));
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

    private News getNewsById(String newsId) {
        return newsRepo.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException("News with id"  + newsId + " not found"));
    }

    private List<NewsDto> convertListNewsToListNewsDto(List<News> news) {
        return news.stream()
                .map(newsToNewsDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
