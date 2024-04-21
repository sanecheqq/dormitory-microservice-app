package com.seminav.marketapp.services;

import com.seminav.marketapp.model.Product;
import com.seminav.marketapp.model.ProductCategory;
import com.seminav.marketapp.model.ProductStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HibernateSearchServiceImpl implements HibernateSearchService {
    private final EntityManager entityManager;
    private final int PAGE_LIMIT = 4;

    @Override
    public List<Product> searchForProducts(String searchPattern, Double minPrice, Double maxPrice, Integer page) {
        SearchSession searchSession = Search.session(entityManager);
        List<Product> result = searchSession.search(Product.class)
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
                ).fetchAllHits();
        return paginate(result, page);
    }

    @Override
    public List<Product> searchForProducts(Double minPrice, Double maxPrice, Integer page) {
        SearchSession searchSession = Search.session(entityManager);
        List<Product> result = searchSession.search(Product.class)
                .where(f -> f.bool()
                        .must(f.range()
                                .field("price")
                                .between(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice)))
                        .must(f.match()
                                .field("status")
                                .matching(ProductStatus.PUBLISHED))
                ).fetchAllHits();
        return paginate(result, page);
    }

    @Override
    public List<Product> searchForProducts(ProductCategory category, Double minPrice, Double maxPrice, Integer page) {
        SearchSession searchSession = Search.session(entityManager);
        List<Product> result = searchSession.search(Product.class)
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
                ).fetchAllHits();
        return paginate(result, page);
    }

    @Override
    public List<Product> searchForProducts(String searchPattern, ProductCategory category, Double minPrice, Double maxPrice, Integer page) {
        SearchSession searchSession = Search.session(entityManager);
        List<Product> result = searchSession.search(Product.class)
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
                ).fetchAllHits();
        return paginate(result, page);
    }

    private List<Product> paginate(List<Product> result, int page) {
        if (page* PAGE_LIMIT >= result.size())
            return List.of();
        else if ((page+1) * PAGE_LIMIT >= result.size())
            return result.subList(page * PAGE_LIMIT, result.size());
        else
            return result.subList(page * PAGE_LIMIT, (page+1) * PAGE_LIMIT);
    }

}
