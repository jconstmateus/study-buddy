package com.studybuddy.backend_java.model;
import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automated (auto-increment)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne // 1 User <-> N Courses
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Empty constructor (data set later)
    public Course() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}