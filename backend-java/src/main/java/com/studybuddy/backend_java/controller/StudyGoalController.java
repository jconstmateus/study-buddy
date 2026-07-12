package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.StudyGoal;
import com.studybuddy.backend_java.service.StudyGoalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/study-goals") // Base URL
public class StudyGoalController {

    // Use of respective service for each request
    private final StudyGoalService studyGoalService;

    public StudyGoalController(StudyGoalService studyGoalService) {
        this.studyGoalService = studyGoalService;
    }

    @PostMapping // POST (create object with information on Body)
    public StudyGoal create(@RequestBody StudyGoal studyGoal) {
        return studyGoalService.save(studyGoal);
    }

    @GetMapping // GET (get a list of objects)
    public List<StudyGoal> findAll() {
        return studyGoalService.findAll();
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public StudyGoal findById(@PathVariable Long id) {
        return studyGoalService.findById(id);
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public void deleteById(@PathVariable Long id) {
        studyGoalService.deleteById(id);
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public StudyGoal update(@PathVariable Long id, @RequestBody StudyGoal studyGoal) {
        studyGoal.setId(id);
        return studyGoalService.save(studyGoal);
    }
}