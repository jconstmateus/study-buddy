package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.Question;
import com.studybuddy.backend_java.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions") // Base URL
public class QuestionController {

    // Use of respective service for each request
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping // POST (create object with information on Body)
    public Question create(@RequestBody Question question) {
        return questionService.save(question);
    }

    @GetMapping // GET (get a list of objects)
    public List<Question> findAll() {
        return questionService.findAll();
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public Question findById(@PathVariable Long id) {
        return questionService.findById(id);
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public void deleteById(@PathVariable Long id) {
        questionService.deleteById(id);
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public Question update(@PathVariable Long id, @RequestBody Question question) {
        question.setId(id);
        return questionService.save(question);
    }
}