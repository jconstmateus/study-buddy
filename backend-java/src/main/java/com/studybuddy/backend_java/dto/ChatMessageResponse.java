package com.studybuddy.backend_java.dto;

import com.studybuddy.backend_java.model.ChatMessage;
import com.studybuddy.backend_java.model.MessageAuthor;

public class ChatMessageResponse {

    private Long id;
    private MessageAuthor author;
    private String text;

    // Build the response DTO from a ChatMessage entity
    public static ChatMessageResponse from(ChatMessage m) {
        ChatMessageResponse dto = new ChatMessageResponse();
        dto.setId(m.getId());
        dto.setAuthor(m.getAuthor());
        dto.setText(m.getText());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MessageAuthor getAuthor() { return author; }
    public void setAuthor(MessageAuthor author) { this.author = author; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}