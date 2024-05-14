package com.seminav.marketapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Indexed
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    @FullTextField
    private String productName;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    @KeywordField
    private ProductCategory category;

    @Column(name = "description")
    @FullTextField
    @Size(max = 1000)
    private String description;

    @Column(name = "price")
    @ScaledNumberField
    private BigDecimal price;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @KeywordField
    private ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addAllImages(List<Image> images) {
        for (Image image : images) {
            this.images.add(image);
            image.setProduct(this);
        }
    }

    public void removeAllImages() {
        for (Image image : this.images) {
            image.setProduct(null);
        }
        this.images.clear();
    }

    public void removeImage(Image image) {
        image.setProduct(null);
        this.images.remove(image);
    }
}
