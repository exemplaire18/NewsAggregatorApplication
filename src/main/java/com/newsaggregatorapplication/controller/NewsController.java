package com.newsaggregatorapplication.controller;

import com.newsaggregatorapplication.dto.ResponseDTO;
import com.newsaggregatorapplication.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<ResponseDTO> searchNews(@RequestParam String keyword, @RequestParam(required = false) Integer page) {
        ResponseDTO response = newsService.search(keyword, page);
        return ResponseEntity.ok(response);
    }

}
