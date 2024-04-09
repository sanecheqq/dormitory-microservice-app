package com.seminav.marketapp.services;

import com.seminav.marketapp.model.Product;
import com.seminav.marketapp.model.ProductCategory;

import java.util.List;

public interface HibernateSearchService {
    List<Product> searchForProducts(Double minPrice, Double maxPrice, Integer page);

    List<Product> searchForProducts(String searchPattern, Double minPrice, Double maxPrice, Integer page);

    List<Product> searchForProducts(ProductCategory category, Double minPrice, Double maxPrice, Integer page);

    List<Product> searchForProducts(String searchPattern, ProductCategory category, Double minPrice, Double maxPrice, Integer page);
}
