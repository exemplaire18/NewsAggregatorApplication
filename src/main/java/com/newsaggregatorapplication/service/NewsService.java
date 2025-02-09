package com.newsaggregatorapplication.service;

import com.newsaggregatorapplication.client.GuardianClient;
import com.newsaggregatorapplication.client.NYTClient;
import com.newsaggregatorapplication.client.NewsClient;
import com.newsaggregatorapplication.config.NewsConfig;
import com.newsaggregatorapplication.dto.ArticleDTO;
import com.newsaggregatorapplication.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final List<NewsClient> newsClients;
    private final NewsConfig config;
    private final GuardianClient guardianClient;
    private final NYTClient nytClient;
    //private final CacheService cacheService;

    public ResponseDTO search(String keyword, Integer page, Integer size) {
        long startTime = System.currentTimeMillis();

        // Fetch articles from clients (with their own pagination logic)
        List<ArticleDTO> guardianArticles = guardianClient.fetchArticles(keyword, page, size);
        List<ArticleDTO> nytArticles = nytClient.fetchArticles(keyword, page, size);

        // Aggregate, deduplicate, and paginate
        List<ArticleDTO> allArticles = Stream.concat(guardianArticles.stream(), nytArticles.stream())
                .collect(Collectors.toMap(ArticleDTO::getUrl, Function.identity(), (a1, a2) -> a1))
                .values().stream()
                .sorted(Comparator.comparing(ArticleDTO::getPublishedDate).reversed())
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();

        // Paginate results (service-level pagination)
        int pageSize = (size != null) ? size : config.getDefaultPageSize();
        int currentPage = (page != null) ? page : 1;
        List<ArticleDTO> paginatedArticles = paginate(allArticles, currentPage, pageSize);

        return buildResponse(paginatedArticles, keyword, currentPage, pageSize, endTime-startTime);

    }

    private List<ArticleDTO> paginate(List<ArticleDTO> articles, int page, int size) {
        int start = (page - 1) * size;
        int end = Math.min(start + size, articles.size());
        return articles.subList(start, end);
    }

    private ResponseDTO buildResponse(List<ArticleDTO> articles, String keyword, int page, int size, long timeTaken) {
        return ResponseDTO.builder()
                .articles(articles)
                .totalPages((int) Math.ceil((double) articles.size() / size))
                .currentPage(page)
                .searchKeyword(keyword)
                .timeTakenMs(timeTaken)
                .build();
    }
}