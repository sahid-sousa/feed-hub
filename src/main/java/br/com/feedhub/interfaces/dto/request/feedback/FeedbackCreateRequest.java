package br.com.feedhub.interfaces.dto.request.feedback;

import java.time.LocalDateTime;

public class FeedbackCreateRequest extends FeedbackDetails {
    public FeedbackCreateRequest(String title, String description, String category, LocalDateTime dateCreated, LocalDateTime lastUpdated) {
        super(title, description, category, dateCreated, lastUpdated);
    }
}
