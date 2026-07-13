package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users") // Base URL
public class UserController {

    // Use of respective service for each request
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping // GET (get a list of objects)
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public User update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.save(user);
    }

}