package com.studybuddy.backend_java.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tests")
public class Test {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automated (auto-increment)
    private Long id;

    @Column(name = "score_obtained")
    private Integer scoreObtained;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @Column(name = "passed")
    private Boolean passed;

    @ManyToOne // A StudyGoal has N tests, Test belongs to 1 StudyGoal
    @JoinColumn(name = "study_goal_id", nullable = false)
    private StudyGoal studyGoal;

    // Empty constructor (data set later)
    public Test() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getScoreObtained() { return scoreObtained; }
    public void setScoreObtained(Integer scoreObtained) { this.scoreObtained = scoreObtained; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getTakenAt() { return takenAt; }
    public void setTakenAt(LocalDateTime takenAt) { this.takenAt = takenAt; }

    public Boolean getPassed() { return passed; }
    public void setPassed(Boolean passed) { this.passed = passed; }

    public StudyGoal getStudyGoal() { return studyGoal; }
    public void setStudyGoal(StudyGoal studyGoal) { this.studyGoal = studyGoal; }
}