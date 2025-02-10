package com.newsaggregatorapplication.service;

import com.newsaggregatorapplication.client.GuardianClient;
import com.newsaggregatorapplication.client.NYTClient;
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
    private final NewsConfig config;
    private final GuardianClient guardianClient;
    private final NYTClient nytClient;
    //private final CacheService cacheService;

    public ResponseDTO search(String keyword, Integer page) {
        long startTime = System.currentTimeMillis();

        // Fetch articles from clients
        List<ArticleDTO> guardianArticles = guardianClient.fetchArticles(keyword);
        List<ArticleDTO> nytArticles = nytClient.fetchArticles(keyword);

        // Aggregate, deduplicate
        List<ArticleDTO> allArticles = Stream.concat(guardianArticles.stream(), nytArticles.stream())
                .collect(Collectors.toMap(ArticleDTO::getUrl, Function.identity(), (a1, a2) -> a1))
                .values().stream()
                .sorted(Comparator.comparing(ArticleDTO::getPublishedDate).reversed())
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();

        // Paginate results (service-level pagination)
        int pageSize = config.getDefaultPageSize();
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

    private List<ArticleDTO> paginate(List<ArticleDTO> articles, int page, int size) {
        int start = (page - 1) * size;
        int end = Math.min(start + size, articles.size());
        return articles.subList(start, end);
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