package br.com.feedhub.application.usecases.feedback;

import br.com.feedhub.interfaces.dto.request.feedback.FeedbackCreateRequest;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CreateFeedback {
    FeedbackResponse execute(FeedbackCreateRequest feedback, HttpServletRequest request);
}
