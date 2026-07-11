package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.Test;
import com.studybuddy.backend_java.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation Service CRUD for each Entity
@Service
public class TestService {

    // Definitive use of respective interface of repository
    private final TestRepository testRepository;

    // Constructor with injection automatized via Spring
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    // Methods
    public Test save(Test test) {
        return testRepository.save(test);
    }

    public List<Test> findAll() {
        return testRepository.findAll();
    }

    public Test findById(Long id) {
        return testRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        testRepository.deleteById(id);
    }
}