package com.seminav.newsapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "news_id")
    private String newsid;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private NewsCategory category;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "address")
    private String address;

    public void addAllImages(List<Image> images) {
        for (Image image : images) {
            this.images.add(image);
            image.setNews(this);
        }
    }

    public void addAllDocuments(List<Document> documents) {
        for (Document document : documents) {
            this.documents.add(document);
            document.setNews(this);
        }
    }

    public void removeAllImages() {
        for (Image image : this.images) {
            image.setNews(null);
        }
        this.images.clear();
    }

    public void removeAllDocuments() {
        for (Document document : this.documents) {
            documents.remove(document);
            document.setNews(null);
        }
    }
}
