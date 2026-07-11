package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.Event;
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

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }
}