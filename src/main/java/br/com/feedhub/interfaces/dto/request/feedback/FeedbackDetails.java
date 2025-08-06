package br.com.feedhub.interfaces.dto.request.feedback;

import br.com.feedhub.domain.feedback.Feedback;

import java.time.LocalDateTime;

public abstract class FeedbackDetails {
    private String title;
    private String description;
    private String category;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;

    public FeedbackDetails() {}

    public FeedbackDetails(String title, String description, String category, LocalDateTime dateCreated, LocalDateTime lastUpdated) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
