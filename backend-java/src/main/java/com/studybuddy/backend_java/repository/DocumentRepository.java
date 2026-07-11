package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

// Extend base interface, associate with Entity and PK
public interface DocumentRepository extends JpaRepository<Document, Long> {
    // TODO add custom queries if necessary
}
