package com.seminav.newsapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @Column(name = "document_id")
    String documentId;

    @Column(name = "url")
    String url;

    @Column(name = "file_name")
    String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private News news;
}
