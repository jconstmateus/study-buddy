package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation Service CRUD for each Entity
@Service
public class UserService {

    // Definitive use of respective interface of repository
    private final UserRepository userRepository;

    // Constructor with injection automatized via Spring
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Methods
    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}