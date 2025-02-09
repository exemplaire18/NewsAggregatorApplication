package com.newsaggregatorapplication.response;

import lombok.Data;

import java.util.List;

@Data
public class GuardianResponse {
    private Response response;

    @Data
    public static class Response {
        private List<Result> results;
    }

    @Data
    public static class Result {
        private String webTitle;
        private String webUrl;
        private String sectionName;
        private String webPublicationDate;
    }
}
