package com.newsaggregatorapplication.service;

import com.newsaggregatorapplication.dto.ArticleDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {
    private final Map<String, List<ArticleDTO>> cache = new ConcurrentHashMap<>();

    public List<ArticleDTO> getFromCache(String keyword) {
        return cache.getOrDefault(keyword, Collections.emptyList());
    }

    public void saveToCache(String keyword, List<ArticleDTO> articles) {
        cache.put(keyword, articles);
    }
}