package com.seminav.newsapp.repositories;

import com.seminav.newsapp.model.News;
import com.seminav.newsapp.model.NewsCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {

    @Query(value = """
        SELECT n from News n
        WHERE (:news_category IS NULL OR n.category = :news_category)
            AND (:search_pattern = 'null' OR n.title LIKE '%'||:search_pattern||'%')
    """)
    List<News> getNewsByNewsCategoryAndSearchPatternAndSortByDate(
            @Param("news_category") NewsCategory category,
            @Param("search_pattern") String searchPattern,
            Sort sort
    );
}
