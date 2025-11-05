package br.com.feedhub.application.usecases.feedback;

import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface FindFeedback {
    FeedbackResponse execute(Long id, HttpServletRequest request);
}
