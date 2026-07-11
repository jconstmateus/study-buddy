package com.studybuddy.backend_java.model;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automated (auto-increment)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String statement;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Column(nullable = false)
    private String options;

    @ManyToOne // A Test has N questions, Question belongs to 1 Test
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    // Empty constructor (data set later)
    public Question() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatement() { return statement; }
    public void setStatement(String statement) { this.statement = statement; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }

    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }
}