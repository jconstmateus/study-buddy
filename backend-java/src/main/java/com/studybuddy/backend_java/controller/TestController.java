package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.Test;
import com.studybuddy.backend_java.service.TestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests") // Base URL
public class TestController {

    // Use of respective service for each request
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping // POST (create object with information on Body)
    public Test create(@RequestBody Test test) {
        return testService.save(test);
    }

    @GetMapping // GET (get a list of objects)
    public List<Test> findAll() {
        return testService.findAll();
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public Test findById(@PathVariable Long id) {
        return testService.findById(id);
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public void deleteById(@PathVariable Long id) {
        testService.deleteById(id);
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public Test update(@PathVariable Long id, @RequestBody Test test) {
        test.setId(id);
        return testService.save(test);
    }
}