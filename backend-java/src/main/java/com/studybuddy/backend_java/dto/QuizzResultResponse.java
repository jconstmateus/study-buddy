package com.studybuddy.backend_java.dto;

import java.util.List;

// Result sent back to the frontend after submitting a quiz
public class QuizzResultResponse {

    private int score;                            // percentage 0-100
    private boolean passed;                       // score >= passingGrade
    private List<QuestionResultResponse> results; // one entry per question

    // Constructor
    public QuizzResultResponse() {}

    // Getters and Setters
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public List<QuestionResultResponse> getResults() { return results; }
    public void setResults(List<QuestionResultResponse> results) { this.results = results; }
}
