package br.com.feedhub.application.usecases.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface ListUserFeedback {
   PageListResponse<FeedbackResponse> execute(
           String title,
           String description,
           String category,
           String status,
           int page,
           int size,
           String sortBy,
           String sortDirection,
           HttpServletRequest request
   );
}
