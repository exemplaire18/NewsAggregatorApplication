package com.newsaggregatorapplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewsAggregatorApplicationTests {
    public static void main(String[] args) {
        new SpringApplicationBuilder(NewsAggregatorApplication.class).build().run(args);
    }

}
