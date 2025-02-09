package com.newsaggregatorapplication.controller;

import com.newsaggregatorapplication.dto.ResponseDTO;
import com.newsaggregatorapplication.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<ResponseDTO> searchNews(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        ResponseDTO response = newsService.search(keyword, page, size);
        //addHateoasLinks(response, keyword, page, size);
        return ResponseEntity.ok(response);
    }

    /*private void addHateoasLinks(NewsResponse response, String keyword, int page, int size) {
        response.addLink("self", buildLink(keyword, page, size));
        if (response.getCurrentPage() < response.getTotalPages()) {
            response.addLink("next", buildLink(keyword, page + 1, size));
        }
        if (response.getCurrentPage() > 1) {
            response.addLink("prev", buildLink(keyword, page - 1, size));
        }
    }

    private String buildLink(String keyword, int page, int size) {
        return String.format("/api/news?keyword=%s&page=%d&size=%d", keyword, page, size);
    }*/
}
