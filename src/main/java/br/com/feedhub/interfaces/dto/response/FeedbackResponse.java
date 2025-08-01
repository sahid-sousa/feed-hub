package br.com.feedhub.interfaces.dto.response;

import br.com.feedhub.interfaces.dto.request.feedback.FeedbackDetails;

import java.time.LocalDateTime;

public class FeedbackResponse extends FeedbackDetails {

    private Long id;
    private Long authorId;
    private String status;

    public FeedbackResponse() {
        super();
    }

    public FeedbackResponse(String title, String description, String category, LocalDateTime dateCreated, LocalDateTime lastUpdated, Long id, Long authorId, String status) {
        super(title, description, category, dateCreated, lastUpdated);
        this.id = id;
        this.authorId = authorId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
