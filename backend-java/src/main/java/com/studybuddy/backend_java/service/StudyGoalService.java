package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.StudyGoal;
import com.studybuddy.backend_java.repository.StudyGoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation Service CRUD for each Entity
@Service
public class StudyGoalService {

    // Definitive use of respective interface of repository
    private final StudyGoalRepository studyGoalRepository;

    // Constructor with injection automatized via Spring
    public StudyGoalService(StudyGoalRepository studyGoalRepository) {
        this.studyGoalRepository = studyGoalRepository;
    }

    // Methods
    public StudyGoal save(StudyGoal studyGoal) {
        return studyGoalRepository.save(studyGoal);
    }

    public List<StudyGoal> findAll() {
        return studyGoalRepository.findAll();
    }

    public StudyGoal findById(Long id) {
        return studyGoalRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        studyGoalRepository.deleteById(id);
    }
}