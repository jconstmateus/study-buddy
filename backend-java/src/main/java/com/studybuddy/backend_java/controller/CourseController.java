package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.exceptions.NotAuthorizedException;
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

    @DeleteMapping("/{id}")
    public ResponseEntity <?> deleteById(@PathVariable Long id, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Course course = courseService.findById(id);

        if (user.getId() == course.getUser().getId()) {
            courseService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new NotAuthorizedException("Not Authorized to Delete This Course");
        }
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public Course updateById(@PathVariable Long id, @RequestBody Course courseModified, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Course course = courseService.findById(id);

        if (user.getId() == course.getUser().getId()) {
            course.setName(courseModified.getName());
            course.setColor(courseModified.getColor());
            return courseService.save(course);
        } else {
            throw new NotAuthorizedException("Not Authorized to Modify This Course");
        }
    }

    @GetMapping // GET (get a list of courses, by user)
    public List<Course> findByUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return courseService.findByUser(user);
    }


}