package com.studybuddy.backend_java.dto;

import java.util.List;

public class QuizzResponse {
    // Get only this info from questions
    private QuizzStatus status;
    private List<QuestionResponse> questions;

    // Constructor
    public QuizzResponse() {}

    // Getters and Setters
    public QuizzStatus getStatus() { return status ; }
    public void setStatus(QuizzStatus status) { this.status = status; }

    public List<QuestionResponse> getQuestions() { return questions; }
    public void setQuestions(List<QuestionResponse> questions) { this.questions = questions; }
}
