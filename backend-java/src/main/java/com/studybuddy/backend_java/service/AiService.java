package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.Question;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final RestClient restClient = RestClient.create("http://localhost:8000");

    // POST - generate a summary with potential context, and summary already existing
    public String summarize(String title, String context, String summary) {
        return restClient.post()
                .uri("/summarize")
                .body(Map.of("title", title, "context", context, "summary", summary))
                .retrieve()
                .body(String.class);
    }

    // GET - generate a quiz with summary and given examples
    public List<Question> generateQuizz(String summary, String examples) {
        return restClient.post()
                .uri("/generate-quizz")
                .body(Map.of("summary", summary, "context", examples))
                .retrieve()
                .body(new ParameterizedTypeReference<List<Question>>() {});
    }
}