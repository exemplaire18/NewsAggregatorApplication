package com.newsaggregatorapplication.service;

import com.newsaggregatorapplication.client.GuardianClient;
import com.newsaggregatorapplication.client.NYTClient;
import com.newsaggregatorapplication.config.NewsConfig;
import com.newsaggregatorapplication.dto.ArticleDTO;
import com.newsaggregatorapplication.dto.ResponseDTO;
import com.newsaggregatorapplication.exception.ServiceUnavailableException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NewsService {
    private final NewsConfig newsConfig;
    private final GuardianClient guardianClient;
    private final NYTClient nytClient;
    private final CacheService cacheService;

    public NewsService(NewsConfig newsConfig, GuardianClient guardianClient, NYTClient nytClient, CacheService cacheService) {
        this.newsConfig = newsConfig;
        this.guardianClient = guardianClient;
        this.nytClient = nytClient;
        this.cacheService = cacheService;
    }

    public ResponseDTO search(String keyword, Integer page) throws ServiceUnavailableException {
        long startTime = System.currentTimeMillis();
        List<ArticleDTO> allArticles;
        if (newsConfig.isOfflineMode()) {
            allArticles = cacheService.getFromCache(keyword);
        } else {
            try {
                List<ArticleDTO> guardianArticles = guardianClient.fetchArticles(keyword);
                List<ArticleDTO> nytArticles = nytClient.fetchArticles(keyword);
                allArticles = Stream.concat(guardianArticles.stream(), nytArticles.stream())
                        .collect(Collectors.toMap(ArticleDTO::getUrl, Function.identity(), (a1, a2) -> a1))
                        .values().stream()
                        .sorted(Comparator.comparing(ArticleDTO::getPublishedDate).reversed())
                        .collect(Collectors.toList());
                cacheService.saveToCache(keyword, allArticles);
            } catch (Exception ex) {
                allArticles = cacheService.getFromCache(keyword);
                if (allArticles.isEmpty()) {
                    throw new ServiceUnavailableException("APIs unavailable, and no cached data found.");
                }
            }
        }

        long endTime = System.currentTimeMillis();

        int pageSize = newsConfig.getDefaultPageSize();
        int currentPage = (page != null) ? page : 1;

        ResponseDTO response = buildResponse(allArticles, keyword, currentPage, pageSize, endTime - startTime);
        addHateoasLinks(response, keyword, currentPage);
        return response;
    }

    private ResponseDTO buildResponse(List<ArticleDTO> allArticles, String keyword, int page, int size, long timeTaken) {
        List<ArticleDTO> paginatedArticles = paginate(allArticles, page, size);
        return ResponseDTO.builder()
                .articles(paginatedArticles)
                .totalPages((int) Math.ceil((double) allArticles.size() / size))
                .currentPage(page)
                .searchKeyword(keyword)
                .timeTakenMs(timeTaken)
                .build();
    }

    private List<ArticleDTO> paginate(List<ArticleDTO> allArticles, int page, int size) {
        int start = (page - 1) * size;
        int end = Math.min(start + size, allArticles.size());
        return allArticles.subList(start, end);
    }

    private void addHateoasLinks(ResponseDTO response, String keyword, int page) {
        response.addLink("self", buildLink(keyword, page));
        if (response.getCurrentPage() < response.getTotalPages()) {
            response.addLink("next", buildLink(keyword, page + 1));
        }
        if (response.getCurrentPage() > 1) {
            response.addLink("prev", buildLink(keyword, page - 1));
        }
    }

    private String buildLink(String keyword, int page) {
        return String.format("/api/news?keyword=%s&page=%d", keyword, page);
    }

}