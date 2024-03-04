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
@Table(name = "images")
public class Image {
    @Id
    @Column(name = "image_id")
    String imageId;

    @Column(name = "url")
    String url;
}
