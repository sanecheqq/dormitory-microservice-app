package com.seminav.marketapp.services;

import com.seminav.marketapp.model.Product;
import com.seminav.marketapp.model.ProductCategory;
import com.seminav.marketapp.model.ProductStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HibernateSearchServiceImpl implements HibernateSearchService {
    private final EntityManager entityManager;

    @Override
    public List<Product> searchForProducts(String searchPattern, Double minPrice, Double maxPrice) {
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Product> result = searchSession.search(Product.class)
                .where(f -> f.bool()
                        .must(f.match()
                                .field("productName").boost(2f)
                                .field("description")
                                .matching(searchPattern)
                                .fuzzy(2, 4))
                        .must(f.range()
                                .field("price")
                                .between(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice)))
                        .must(f.match()
                                .field("status")
                                .matching(ProductStatus.PUBLISHED))
                )
                .fetch(10);
        System.out.println(result.total().hitCount());
        return result.hits();
    }

    @Override
    public List<Product> searchForProducts(Double minPrice, Double maxPrice) {
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Product> result = searchSession.search(Product.class)
                .where(f -> f.bool()
                        .must(f.range()
                                .field("price")
                                .between(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice)))
                        .must(f.match()
                                .field("status")
                                .matching(ProductStatus.PUBLISHED))
                )
                .fetch(10);
        System.out.println(result.total().hitCount());
        return result.hits();
    }

    @Override
    public List<Product> searchForProducts(ProductCategory category, Double minPrice, Double maxPrice) {
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Product> result = searchSession.search(Product.class)
                .where(f -> f.bool()
                        .must(f.match()
                                .field("category")
                                .matching(category))
                        .must(f.range()
                                .field("price")
                                .between(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice)))
                        .must(f.match()
                                .field("status")
                                .matching(ProductStatus.PUBLISHED))
                )
                .fetch(10);
        System.out.println(result.total().hitCount());
        return result.hits();
    }

    @Override
    public List<Product> searchForProducts(String searchPattern, ProductCategory category, Double minPrice, Double maxPrice) {
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Product> result = searchSession.search(Product.class)
                .where(f -> f.bool()
                        .must(f.match()
                                .field("productName").boost(2f)
                                .field("description")
                                .matching(searchPattern)
                                .fuzzy(2, 4)).must(f.match()
                                .field("category")
                                .matching(category))
                        .must(f.range()
                                .field("price")
                                .between(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice)))
                        .must(f.match()
                                .field("status")
                                .matching(ProductStatus.PUBLISHED))
                )
                .fetch(10);
        System.out.println(result.total().hitCount());
        return result.hits();       }

}
