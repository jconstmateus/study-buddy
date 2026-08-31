package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.Event;
import com.studybuddy.backend_java.model.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Extend base interface, associate with Entity and PK
public interface StudyGoalRepository extends JpaRepository<StudyGoal, Long> {

    Optional<StudyGoal> findByEvent(Event event);

    // TODO add custom queries if necessary
}
