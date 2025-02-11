package com.newsaggregatorapplication.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI newsAggregatorOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("News Aggregator API")
                        .description("""
                    API for searching and aggregating news articles from Guardian and New York Times.
                    ## Error Codes
                    - `400 Bad Request`: Invalid input parameters.
                    - `404 Not Found`: No articles found.
                    - `500 Internal Server Error`: Backend service failure.
                    """)
                        .version("1.0"))
                .components(new Components()
                        .addResponses("BadRequest",
                                new ApiResponse().description("Invalid request parameters"))
                        .addResponses("NotFound",
                                new ApiResponse().description("No articles found"))
                        .addResponses("InternalError",
                                new ApiResponse().description("Server-side error"))
                        .addResponses("ServiceUnavailable",
                                new ApiResponse().description("Offline mode with no cached data"))
                );
    }
}