package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.Test;
import com.studybuddy.backend_java.model.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

// Extend base interface, associate with Entity and PK
public interface TestRepository extends JpaRepository<Test, Long> {
    Test findFirstByStudyGoalOrderByCreatedAtDesc(StudyGoal studyGoal);
    // TODO add custom queries if necessary
}
