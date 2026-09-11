package com.studybuddy.backend_java.dto;

import com.studybuddy.backend_java.model.Question;

public class QuestionResponse {

    private Long id;
    private String statement;
    private String options;

    // Constructor
    public QuestionResponse() {}

    // Build the response DTO from a Question entity (correctAnswer stays only sserver-side)
    public QuestionResponse (Question q) {
        this.id = q.getId();
        this.statement = q.getStatement();
        this.options = q.getOptions();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatement() { return statement; }
    public void setStatement(String statement) { this.statement = statement; }

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
}
