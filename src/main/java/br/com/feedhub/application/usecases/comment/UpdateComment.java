package br.com.feedhub.application.usecases.comment;

import br.com.feedhub.interfaces.dto.request.comment.CommentUpdateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UpdateComment {
    CommentResponse execute(CommentUpdateRequest commentUpdateRequest, HttpServletRequest request);
}
