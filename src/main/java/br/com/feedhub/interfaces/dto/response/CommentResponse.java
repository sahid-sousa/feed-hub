package br.com.feedhub.interfaces.dto.response;

import br.com.feedhub.interfaces.dto.request.comment.CommentDetails;

import java.time.LocalDateTime;

public class CommentResponse extends CommentDetails {

    private Long id;
    private Long feedbackId;
    private Long authorId;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;

    public CommentResponse() {}

    public CommentResponse(Long feedbackId, String content, Long id, Long authorId, LocalDateTime dateCreated, LocalDateTime lastUpdated) {
        super(content);
        this.id = id;
        this.authorId = authorId;
        this.feedbackId = feedbackId;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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
