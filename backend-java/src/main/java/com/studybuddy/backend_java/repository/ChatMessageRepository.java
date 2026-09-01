package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.ChatMessage;
import com.studybuddy.backend_java.model.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Extend base interface, associate with Entity and PK
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByStudyGoalOrderByTimestampAsc(StudyGoal studyGoal);
    // TODO add custom queries if necessary
}
