package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.exceptions.ResourceNotFoundException;
import com.studybuddy.backend_java.model.Course;
import com.studybuddy.backend_java.model.Event;
import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation Service CRUD for each Entity
@Service
public class EventService {

    // Definitive use of respective interface of repository
    private final EventRepository eventRepository;

    // Constructor with injection automatized via Spring
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Methods
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> findByCourse(Course course) {
        return eventRepository.findByCourse(course);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    public List<Event> findByCourseUser(User user) {
        return eventRepository.findByCourseUser(user);
    }
}