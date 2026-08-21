package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.Course;
import com.studybuddy.backend_java.model.Event;
import com.studybuddy.backend_java.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Extend base interface, associate with Entity and PK
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCourse(Course course);
    List<Event> findByCourseUser(User user);
    // TODO add custom queries if necessary
}
