package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.Question;
import com.studybuddy.backend_java.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation Service CRUD for each Entity
@Service
public class QuestionService {

    // Definitive use of respective interface of repository
    private final QuestionRepository questionRepository;

    // Constructor with injection automatized via Spring
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // Methods
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }
}