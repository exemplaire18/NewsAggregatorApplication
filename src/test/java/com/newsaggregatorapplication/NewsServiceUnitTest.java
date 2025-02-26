package com.newsaggregatorapplication;

import com.newsaggregatorapplication.client.GuardianClient;
import com.newsaggregatorapplication.client.NYTClient;
import com.newsaggregatorapplication.config.NewsConfig;
import com.newsaggregatorapplication.dto.ArticleDTO;
import com.newsaggregatorapplication.dto.ResponseDTO;
import com.newsaggregatorapplication.service.CacheService;
import com.newsaggregatorapplication.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceUnitTest {
    @Mock
    private GuardianClient guardianClient;

    @Mock
    private NYTClient nytClient;

    @Mock
    private NewsConfig newsConfig;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    void setup(){
        when(newsConfig.isOfflineMode()).thenReturn(false);
        when(newsConfig.getDefaultPageSize()).thenReturn(10);
    }

    @Test
    void testSearch_WithDeduplication() {
        ArticleDTO article1 = ArticleDTO.builder()
                .url("url1")
                .headline("Apple News 1")
                .publishedDate("11/2/25")
                .build();
        ArticleDTO article2 = ArticleDTO.builder()
                .url("url2")
                .headline("Apple News 2")
                .publishedDate("9/2/25")
                .build();
        ArticleDTO article3 = ArticleDTO.builder()
                .url("url1")
                .headline("Apple News 1 (NYT)")
                .publishedDate("10/2/25")
                .build();

        when(guardianClient.fetchArticles(eq("apple"))).thenReturn(List.of(article1, article2));
        when(nytClient.fetchArticles(eq("apple"))).thenReturn(List.of(article3));

        ResponseDTO response = newsService.search("apple", 1);

        // Verify deduplication (3 articles fetched, 2 after dedupe)
        assertEquals(2, response.getArticles().size());
        assertEquals(1, response.getTotalPages());
    }

    @Test
    void testPaginate_EdgeCases() {
        List<ArticleDTO> articles1 = IntStream.range(0, 15)
                .mapToObj(i -> ArticleDTO.builder().url("url" + i).publishedDate("10/2/25").build())
                .toList();
        List<ArticleDTO> articles2 = IntStream.range(0, 10)
                .mapToObj(i -> ArticleDTO.builder().url("url" + i+14).publishedDate("10/2/25").build())
                .toList();
        when(guardianClient.fetchArticles(eq("apple"))).thenReturn(articles1);
        when(nytClient.fetchArticles(eq("apple"))).thenReturn(articles2);

        ResponseDTO response1 = newsService.search("apple", 1);
        assertEquals(10, response1.getArticles().size());

        // Page 3 (size 10, only 5 items left)
        ResponseDTO response2 = newsService.search("apple", 3);
        assertEquals(5, response2.getArticles().size());
    }
}