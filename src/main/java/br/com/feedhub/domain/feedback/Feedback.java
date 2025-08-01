package br.com.feedhub.domain.feedback;

import br.com.feedhub.domain.security.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_generator")
    @SequenceGenerator(name = "feedback_generator", sequenceName = "feedback_seq", allocationSize = 1)
    private Long id;
    private String title;

    @ManyToOne
    private User author;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private FeedbackCategory category;

    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

    private LocalDateTime dateCreated = LocalDateTime.now();
    private LocalDateTime lastUpdated = LocalDateTime.now();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FeedbackCategory getCategory() {
        return category;
    }

    public void setCategory(FeedbackCategory category) {
        this.category = category;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
