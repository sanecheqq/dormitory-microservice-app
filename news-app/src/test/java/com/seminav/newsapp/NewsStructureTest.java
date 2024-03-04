package com.seminav.newsapp;

import com.seminav.newsapp.model.Document;
import com.seminav.newsapp.model.Image;
import com.seminav.newsapp.model.News;
import com.seminav.newsapp.model.NewsCategory;
import com.seminav.newsapp.repositories.DocumentRepository;
import com.seminav.newsapp.repositories.ImageRepository;
import com.seminav.newsapp.repositories.NewsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class NewsStructureTest {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void whenCreateNewsWithImagesAndDocuments_thenInsertWithUpdates() {
        News news = new News();
        news.setTitle("Title");
        news.setContent("Content");
        news.setCategory(NewsCategory.NEWS);
        news.setDate(Timestamp.valueOf(LocalDateTime.now()));
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Image image = new Image();
            image.setImageId("id" + i);
            image.setUrl("url" + i);
            images.add(image);
        }
        List<Document> docs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Document document = new Document();
            document.setDocumentId("id" + i);
            document.setUrl("url" + i);
            docs.add(document);
        }
        news.setImages(images);
        news.setDocuments(docs);
        newsRepository.save(news);
        assertThat(documentRepository.count()).isEqualTo(3);
        assertThat(imageRepository.count()).isEqualTo(3);
    }
}
