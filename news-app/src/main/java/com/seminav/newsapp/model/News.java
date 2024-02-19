package com.seminav.newsapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "news")
@RequiredArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @Column(name = "title")
    String title;

    @Column(name = "content")
    String content;

    @Column(name = "category")
    NewsCategory newsCategory;

    @Column(name = "image_id")
    String imageId;

    @Column(name = "date")
    Timestamp date;
}
