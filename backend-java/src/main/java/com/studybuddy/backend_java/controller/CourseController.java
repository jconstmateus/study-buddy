package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.Course;
import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.service.CourseService;
import com.studybuddy.backend_java.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses") // Base URL
public class CourseController {

    // Use of respective service for each request
    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping
    public Course create(@RequestBody Course course, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        course.setUser(user);
        return courseService.save(course);
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

    @GetMapping("/me") // GET (current user)
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(user);
    }

    @GetMapping // GET (get a list of courses, by user)
    public List<Course> findByUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return courseService.findByUser(user);
    }


}