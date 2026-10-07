package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.Question;
import com.studybuddy.backend_java.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Extend base interface, associate with Entity and PK
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTest(Test test);
    // TODO add custom queries if necessary
}

