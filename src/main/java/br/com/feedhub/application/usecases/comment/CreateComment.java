package br.com.feedhub.application.usecases.comment;

import br.com.feedhub.interfaces.dto.request.comment.CommentCreateRequest;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CreateComment {
    CommentResponse execute(CommentCreateRequest comment, HttpServletRequest request);
}
