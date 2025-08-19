package br.com.feedhub.interfaces.dto.request.comment;

public class CommentCreateRequest extends CommentDetails {
    private Long feedbackId;

    public CommentCreateRequest() {}

    public CommentCreateRequest(Long feedbackId, String content) {
        super(content);
        this.feedbackId = feedbackId;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
}
