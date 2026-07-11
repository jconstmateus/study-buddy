package com.studybuddy.backend_java.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automated (auto-increment)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageAuthor author;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne // A StudyGoal has N chat messages, ChatMessage belongs to 1 StudyGoal
    @JoinColumn(name = "study_goal_id", nullable = false)
    private StudyGoal studyGoal;

    // Empty constructor (data set later)
    public ChatMessage() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MessageAuthor getAuthor() { return author; }
    public void setAuthor(MessageAuthor author) { this.author = author; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public StudyGoal getStudyGoal() { return studyGoal; }
    public void setStudyGoal(StudyGoal studyGoal) { this.studyGoal = studyGoal; }
}