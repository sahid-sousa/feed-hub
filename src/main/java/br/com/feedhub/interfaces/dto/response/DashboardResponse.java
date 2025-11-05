package br.com.feedhub.interfaces.dto.response;

import br.com.feedhub.infrastructure.repository.comment.CommentMonthCount;
import br.com.feedhub.infrastructure.repository.feedback.FeedbackMonthCount;

import java.util.List;

public class DashboardResponse {

    Integer feedbacks;
    Integer comments;
    List<FeedbackMonthCount> feedbackMonthCounts;
    List<CommentMonthCount> commentMonthCounts;

    public DashboardResponse() {}

    public DashboardResponse(Integer feedbacks, Integer comments, List<FeedbackMonthCount> feedbackMonthCounts, List<CommentMonthCount> commentMonthCounts) {
        this.feedbacks = feedbacks;
        this.comments = comments;
        this.feedbackMonthCounts = feedbackMonthCounts;
        this.commentMonthCounts = commentMonthCounts;
    }

    public Integer getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Integer feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public List<FeedbackMonthCount> getFeedbackMonthCounts() {
        return feedbackMonthCounts;
    }

    public void setFeedbackMonthCounts(List<FeedbackMonthCount> feedbackMonthCounts) {
        this.feedbackMonthCounts = feedbackMonthCounts;
    }

    public List<CommentMonthCount> getCommentMonthCounts() {
        return commentMonthCounts;
    }

    public void setCommentMonthCounts(List<CommentMonthCount> commentMonthCounts) {
        this.commentMonthCounts = commentMonthCounts;
    }
}
