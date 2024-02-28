package com.seminav.newsapp.configuration;

import com.seminav.newsapp.model.News;
import com.seminav.newsapp.model.NewsCategory;
import com.seminav.newsapp.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements ApplicationRunner {

    private final NewsRepository newsRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws InterruptedException {
        if (newsRepository.count() == 0) {
            fillDatabase();
        }
    }

    private void fillDatabase() throws InterruptedException {
//        Random random = new Random();
        var news1 = new News(null, "Happy and sad boy", "aaaa", NewsCategory.NEWS, "abcd123", Timestamp.from(Instant.now()));
        Thread.sleep(100);
        var news2 = new News(null, "Aaaa bbb article", "hello student", NewsCategory.ORDERS, "abcd124", Timestamp.from(Instant.now()));
        Thread.sleep(100);
        var news3 = new News(null, "Crying student on session", "bb student otchislen", NewsCategory.NEWS, "abcd125", Timestamp.from(Instant.now()));

        newsRepository.saveAll(List.of(news1, news2, news3));
    }
}