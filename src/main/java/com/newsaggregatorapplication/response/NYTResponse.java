package com.newsaggregatorapplication.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NYTResponse {
    private Response response;
    @Data
    public static class Response {
        private List<Doc> docs;
    }

    @Data
    public static class Doc {
        @JsonProperty("abstract")
        private String summary;

        @JsonProperty("web_url")
        private String webUrl;
        private Headline headline;
        @JsonProperty("pub_date")
        private String pubDate;
    }

    @Data
    public static class Headline {
        private String main;
    }
}
