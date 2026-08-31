package com.studybuddy.backend_java.dto;

import jakarta.validation.constraints.NotBlank;

public class SummarizeSubjectRequest {

    private String context;

    public SummarizeSubjectRequest() {}

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
}
