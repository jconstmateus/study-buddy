package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.dto.StatusChangeRequest;
import com.studybuddy.backend_java.exceptions.NotAuthorizedException;
import com.studybuddy.backend_java.model.Course;
import com.studybuddy.backend_java.model.Event;
import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.service.EventService;
import com.studybuddy.backend_java.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events") // Base URL
public class EventController {

    // Use of respective service for each request
    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public ResponseEntity<?> deleteById(@PathVariable Long id, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Event event = eventService.findById(id);

        if (user.getId().equals(event.getCourse().getUser().getId())) {
            eventService.deleteById(id);
            return ResponseEntity.ok().build();

        } else {
            throw new NotAuthorizedException("Not Authorized to Delete This Event");
        }
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody StatusChangeRequest newEventStatus, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Event event = eventService.findById(id);

        if (user.getId().equals(event.getCourse().getUser().getId())) {
            event.setEventStatus(newEventStatus.getNewEventStatus());
            eventService.save(event);
            return ResponseEntity.ok(event.getEventStatus());

        } else {
            throw new NotAuthorizedException("Not Authorized to Modify This Event");
        }
    }

    @GetMapping // GET (list of all events)
    public List<Event> findAllEvents(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return eventService.findByCourseUser(user);
    }

}