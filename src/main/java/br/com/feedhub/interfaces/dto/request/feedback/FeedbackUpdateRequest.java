package br.com.feedhub.interfaces.dto.request.feedback;

import java.time.LocalDateTime;

public class FeedbackUpdateRequest extends FeedbackDetails {

    private Long id;

    public FeedbackUpdateRequest() {
        super();
    }

    public FeedbackUpdateRequest(String title, String description, String category, LocalDateTime dateCreated, LocalDateTime lastUpdated, Long id) {
        super(title, description, category, dateCreated, lastUpdated);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
