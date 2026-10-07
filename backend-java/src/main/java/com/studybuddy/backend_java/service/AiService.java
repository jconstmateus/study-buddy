package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.ChatMessage;
import com.studybuddy.backend_java.model.Question;
import com.studybuddy.backend_java.model.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
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


    // POST - generate a response with a new message
    public String createResponse(String summary, List<ChatMessage> chat) {

        // Give the AI service only the information needed (faster and protect password)
        List<Map<String, String>> lines = new ArrayList<>();
        for (ChatMessage m: chat) {
            lines.add(Map.of("author", m.getAuthor().name(), "text", m.getText() ));
        }

        return restClient.post()
                .uri("/new-message")
                .body(Map.of("summary", summary != null ? summary : "", "chat", lines))
                .retrieve()
                .body(String.class);
    }


    // POST - generate questions for quizz with IA, and correct it into complete List<Question>
    public List<Question> generateQuestionsForQuizz(Test test) {
        String summary = test.getStudyGoal().getSummary();

        // The quiz is built from the summary; without it there is nothing to ask about
        // (also: Map.of does not allow null values and would throw NullPointerException)
        if (summary == null || summary.isBlank()) {
            throw new IllegalStateException("Cannot generate a quiz: this study goal has no summary yet.");
        }

        List<Question> questions = restClient.post()
                .uri("/generate-questions")
                .body(Map.of("summary", summary))
                .retrieve()
                .body(new ParameterizedTypeReference<List<Question>>() {}); // Return a list of Questions

        if (questions != null) {
            for (Question q: questions) {
                q.setTest(test);
            }
        }

        return questions;
    }
}