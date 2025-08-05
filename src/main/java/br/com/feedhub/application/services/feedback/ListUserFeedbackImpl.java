package br.com.feedhub.application.services.feedback;

import br.com.feedhub.adapters.database.feedback.FeedbackGateway;
import br.com.feedhub.adapters.database.user.UserGateway;
import br.com.feedhub.application.usecases.feedback.ListUserFeedback;
import br.com.feedhub.application.usecases.security.auth.ExtractUsername;
import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import br.com.feedhub.interfaces.dto.response.FeedbackResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import br.com.feedhub.interfaces.exceptions.RequiredObjectIsNullException;
import br.com.feedhub.utils.GenericBuilder;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class ListUserFeedbackImpl implements ListUserFeedback {

    private final ExtractUsername extractUsername;
    private final FeedbackGateway feedbackGateway;
    private final UserGateway userGateway;

    public ListUserFeedbackImpl(ExtractUsername extractUsername, FeedbackGateway feedbackGateway, UserGateway userGateway) {
        this.extractUsername = extractUsername;
        this.feedbackGateway = feedbackGateway;
        this.userGateway = userGateway;
    }

    @Override
    public PageListResponse<FeedbackResponse> execute(
            String title,
            String description,
            String category,
            String status,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            HttpServletRequest request
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        String username = extractUsername.execute(request.getHeader("Authorization"));
        Optional<User> author = userGateway.findByUsername(username);
        if (author.isEmpty()) {
            throw new RequiredObjectIsNullException("Author with id: " + username + " not found");
        }
        String titleFilter = StringUtils.hasText(title) ? title : "";
        String descriptionFilter = StringUtils.hasText(description) ? description : "";
        String categoryFilter = StringUtils.hasText(category) ? category : "";
        String statusFilter = StringUtils.hasText(status) ? status : "";
        Page<Feedback> feedbacksPage = feedbackGateway.findAllByFilters(author.get(), titleFilter, descriptionFilter, categoryFilter, statusFilter, pageable);
        Page<FeedbackResponse> responsePage = feedbacksPage.map(feedback ->
                GenericBuilder.of(FeedbackResponse::new)
                        .with(FeedbackResponse::setId, feedback.getId())
                        .with(FeedbackResponse::setTitle, feedback.getTitle())
                        .with(FeedbackResponse::setDescription, feedback.getDescription())
                        .with(FeedbackResponse::setAuthorId, author.get().getId())
                        .with(FeedbackResponse::setCategory, feedback.getCategory().name())
                        .with(FeedbackResponse::setStatus, feedback.getStatus().name())
                        .with(FeedbackResponse::setDateCreated, feedback.getDateCreated())
                        .with(FeedbackResponse::setLastUpdated, feedback.getLastUpdated())
                        .build()
        );
        return PageListResponse.of(responsePage);
    }
}
