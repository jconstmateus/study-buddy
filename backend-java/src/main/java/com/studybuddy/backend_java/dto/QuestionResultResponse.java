package com.studybuddy.backend_java.dto;

// One line of the quiz grading: whether this question was answered correctly
public class QuestionResultResponse {

    private Long questionId;
    private boolean correct;

    // Constructor
    public QuestionResultResponse() {}

    public QuestionResultResponse(Long questionId, boolean correct) {
        this.questionId = questionId;
        this.correct = correct;
    }

    // Getters and Setters
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
