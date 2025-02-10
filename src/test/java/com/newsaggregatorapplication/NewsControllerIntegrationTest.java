package com.newsaggregatorapplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class NewsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSearchEndpoint_Success() throws Exception {
        mockMvc.perform(get("/api/news")
                        .param("keyword", "apple"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchEndpoint_MissingKeyword() throws Exception {
        mockMvc.perform(get("/api/news"))
                .andExpect(status().isBadRequest());
    }
}