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

//        System.out.println(result.total().hitCount());
//        return result.hits();
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
//                .fetch(page,4);
//        System.out.println(result.total().hitCount());
//        System.out.println(result);
//        return result.hits();
    }

    @Override
    public List<Product> searchForProducts(ProductCategory category, Double minPrice, Double maxPrice, Integer page) {
        SearchSession searchSession = Search.session(entityManager);
        return searchSession.search(Product.class)
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
                ).fetchHits(page*4, 4);

//        System.out.println(result.total().hitCount());
//        return result.hits();
    }

    @Override
    public List<Product> searchForProducts(String searchPattern, ProductCategory category, Double minPrice, Double maxPrice, Integer page) {
        SearchSession searchSession = Search.session(entityManager);
        return searchSession.search(Product.class)
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
                ).fetchHits(page*4, 4);

//        System.out.println(result.total().hitCount());
//        return result.hits();
    }

    private List<Product> paginate(List<Product> result, int page) {
        final int limit = 4;
        if (result.size() <= limit)
            return result;
        if (page*limit >= result.size())
            return List.of();
        else if ((page+1)*limit >= result.size())
            return result.subList(page*limit, result.size());
        else
            return result.subList(page*limit, (page+1)*limit);
    }

}