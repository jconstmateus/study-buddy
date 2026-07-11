package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.Course;
import com.studybuddy.backend_java.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation Service CRUD for each Entity
@Service
public class CourseService {

    // Definitive use of respective interface of repository
    private final CourseRepository courseRepository;

    // Constructor with injection automatized via Spring
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Methods
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }
}