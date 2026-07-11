package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

// Extend base interface, associate with Entity and PK
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // TODO add custom queries if necessary
}

