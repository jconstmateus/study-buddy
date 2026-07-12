package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.ChatMessage;
import com.studybuddy.backend_java.service.ChatMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat-messages") // Base URL
public class ChatMessageController {

    // Use of respective service for each request
    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping // POST (create object with information on Body)
    public ChatMessage create(@RequestBody ChatMessage chatMessage) {
        return chatMessageService.save(chatMessage);
    }

    @GetMapping // GET (get a list of objects)
    public List<ChatMessage> findAll() {
        return chatMessageService.findAll();
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public ChatMessage findById(@PathVariable Long id) {
        return chatMessageService.findById(id);
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public void deleteById(@PathVariable Long id) {
        chatMessageService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ChatMessage update(@PathVariable Long id, @RequestBody ChatMessage chatMessage) {
        chatMessage.setId(id);
        return chatMessageService.save(chatMessage);
    }
}