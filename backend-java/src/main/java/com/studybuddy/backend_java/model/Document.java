package com.studybuddy.backend_java.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// New Entity, name definition
@Entity
@Table(name = "documents")
public class Document {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automated (auto-increment)
    private Long id;

    @Column(name = "file_url", nullable = false)         // Cannot be null
    private String fileUrl;

    @Column(nullable = false)
    private String type;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne // A StudyGoal has N documents, Document belongs to 1 StudyGoal
    @JoinColumn(name = "study_goal_id", nullable = false)
    private StudyGoal studyGoal;

    // Empty constructor (data set later)
    public Document() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public StudyGoal getStudyGoal() { return studyGoal; }
    public void setStudyGoal(StudyGoal studyGoal) { this.studyGoal = studyGoal; }
}