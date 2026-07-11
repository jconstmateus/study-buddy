package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

// Extend base interface, associate with Entity and PK
public interface EventRepository extends JpaRepository<Event, Long> {
    // TODO add custom queries if necessary
}
