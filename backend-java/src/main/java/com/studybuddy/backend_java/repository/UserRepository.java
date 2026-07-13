package com.studybuddy.backend_java.repository;

import com.studybuddy.backend_java.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Extend base interface, associate with Entity and PK
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    // TODO add custom queries if necessary
}
