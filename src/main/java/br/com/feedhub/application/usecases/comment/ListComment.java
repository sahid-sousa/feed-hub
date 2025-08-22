package br.com.feedhub.application.usecases.comment;

import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface ListComment {
    PageListResponse<CommentResponse> execute(
            Long feedbackId,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            HttpServletRequest request
    );
}
