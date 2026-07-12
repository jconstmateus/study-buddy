package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.Course;
import com.studybuddy.backend_java.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses") // Base URL
public class CourseController {

    // Use of respective service for each request
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping // POST (create object with information on Body)
    public Course create(@RequestBody Course course) {
        return courseService.save(course);
    }

    @GetMapping // GET (get a list of objects)
    public List<Course> findAll() {
        return courseService.findAll();
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public Course findById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public void deleteById(@PathVariable Long id) {
        courseService.deleteById(id);
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public Course update(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        return courseService.save(course);
    }
}