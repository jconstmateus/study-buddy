package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

// Extend base interface, associate with Entity and PK
public interface StudyGoalRepository extends JpaRepository<StudyGoal, Long> {
    // TODO add custom queries if necessary
}
