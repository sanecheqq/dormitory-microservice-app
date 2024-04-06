package com.seminav.marketapp.repositories;

import com.seminav.marketapp.model.Product;
import com.seminav.marketapp.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByStatus(ProductStatus status);
}
