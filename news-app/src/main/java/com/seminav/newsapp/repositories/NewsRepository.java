package com.seminav.newsapp.repositories;

import com.seminav.newsapp.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {
}
