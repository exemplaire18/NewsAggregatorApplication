package com.newsaggregatorapplication.client;

import com.newsaggregatorapplication.dto.ArticleDTO;

import java.util.List;

public interface NewsClient{
    List<ArticleDTO> fetchArticles(String keyword);
}
