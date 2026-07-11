package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.ChatMessage;
import com.studybuddy.backend_java.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation Service CRUD for each Entity
@Service
public class ChatMessageService {

    // Definitive use of respective interface of repository
    private final ChatMessageRepository chatMessageRepository;

    // Constructor with injection automatized via Spring
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // Methods
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findAll() {
        return chatMessageRepository.findAll();
    }

    public ChatMessage findById(Long id) {
        return chatMessageRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        chatMessageRepository.deleteById(id);
    }
}