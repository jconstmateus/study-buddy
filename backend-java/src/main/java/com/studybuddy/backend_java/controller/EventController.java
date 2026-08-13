package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.dto.StatusChangeRequest;
import com.studybuddy.backend_java.exceptions.NotAuthorizedException;
import com.studybuddy.backend_java.model.Course;
import com.studybuddy.backend_java.model.Event;
import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.service.CourseService;
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
    private final CourseService courseService;

    public EventController(EventService eventService, UserService userService, CourseService courseService) {
        this.eventService = eventService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @PostMapping("/by-course/{id}")
    public Event create(@RequestBody Event event, @PathVariable Long id, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Course course = courseService.findById(id);

        if (user.getId().equals(course.getUser().getId())) {
            event.setCourse(course);
            return eventService.save(event);
        } else {
            throw new NotAuthorizedException("Not Authorized to Add Events to This Course");
        }
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public List<Event> findbyCourse(@PathVariable Long id, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Course course = courseService.findById(id);

        if (user.getId().equals(course.getUser().getId())) {
            return eventService.findByCourse(course);
        } else {
            throw new NotAuthorizedException("Not Authorized to Modify This Course");
        }
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

}