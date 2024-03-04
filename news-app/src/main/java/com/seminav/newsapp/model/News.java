package com.seminav.newsapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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

    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    @Column(name = "date")
    private Timestamp date;
}
