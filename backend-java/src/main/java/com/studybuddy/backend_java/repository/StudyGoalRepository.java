package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.Event;
import com.studybuddy.backend_java.model.StudyGoal;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// Extend base interface, associate with Entity and PK
public interface StudyGoalRepository extends JpaRepository<StudyGoal, Long> {

    Optional<StudyGoal> findByEvent(Event event);
    // Treat race condition
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from StudyGoal s where s.event = :event")
    Optional<StudyGoal> findByEventForUpdate(Event event);

    // TODO add custom queries if necessary
}
