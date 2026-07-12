package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.Event;
import com.studybuddy.backend_java.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events") // Base URL
public class EventController {

    // Use of respective service for each request
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping // POST (create object with information on Body)
    public Event create(@RequestBody Event event) {
        return eventService.save(event);
    }

    @GetMapping // GET (get a list of objects)
    public List<Event> findAll() {
        return eventService.findAll();
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public Event findById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public void deleteById(@PathVariable Long id) {
        eventService.deleteById(id);
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public Event update(@PathVariable Long id, @RequestBody Event event) {
        event.setId(id);
        return eventService.save(event);
    }
}