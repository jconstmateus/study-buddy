package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

// Extend base interface, associate with Entity and PK
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // TODO add custom queries if necessary
}
