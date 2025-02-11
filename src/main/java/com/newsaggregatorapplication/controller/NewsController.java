package com.newsaggregatorapplication.controller;

import com.newsaggregatorapplication.dto.ResponseDTO;
import com.newsaggregatorapplication.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "News API", description = "Search and paginate news articles")
@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    @Operation(summary = "Search news articles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound"),
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalError"),
            @ApiResponse(responseCode = "503", ref = "#/components/responses/ServiceUnavailable")
    })
    @GetMapping
    public ResponseEntity<ResponseDTO> searchNews(
            @Parameter(description = "Search keyword (e.g., 'apple')", required = true)
            @RequestParam String keyword,
            @Parameter(description = "Page number (default: 1)")
            @RequestParam(required = false) Integer page) {
        ResponseDTO response = newsService.search(keyword, page);
        return ResponseEntity.ok(response);
    }

}
