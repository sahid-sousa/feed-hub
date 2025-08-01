package br.com.feedhub.application.usecases.feedback;

import br.com.feedhub.interfaces.dto.request.feedback.FeedbackUpdateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UpdateFeedback {
    FeedbackResponse execute(FeedbackUpdateRequest feedback, HttpServletRequest request);
}
