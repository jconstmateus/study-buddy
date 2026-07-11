package com.studybuddy.backend_java.model;
import jakarta.persistence.*;

@Entity
@Table(name = "study_goals")
public class StudyGoal {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automated (auto-increment)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column
    private boolean learned;

    @Column(name = "passing_grade")
    private Integer passingGrade;

    @OneToOne // 1 StudyGoal corresponds to 1 Event of type STUDY_GOAL, and vice-versa
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Empty constructor (data set later)
    public StudyGoal() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public boolean isLearned() { return learned; }
    public void setLearned(boolean learned) { this.learned = learned; }

    public Integer getPassingGrade() { return passingGrade; }
    public void setPassingGrade(Integer passingGrade) { this.passingGrade = passingGrade; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}