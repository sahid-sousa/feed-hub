package br.com.feedhub.infrastructure.repository.feedback;

import br.com.feedhub.domain.feedback.Feedback;
import br.com.feedhub.domain.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends Repository<Feedback, Long> {
    Feedback save(Feedback feedback);
    Optional<Feedback> findById(Long id);
    Optional<Feedback> findByIdAndAuthor(Long id, User author);
    Optional<List<Feedback>> findAllByAuthor(User author, Pageable pageable);
    Integer countByAuthor(User author);
    @Query("SELECT f.month AS month, COUNT(f.id) AS count FROM Feedback f WHERE f.author = :author AND f.month BETWEEN :startMonth AND :endMonth GROUP BY f.month")
    List<FeedbackMonthCount> findAllByUserAndGroupMonth(
            @Param("author") User author,
            @Param("startMonth") Integer startMonth,
            @Param("endMonth") Integer endMonth
    );
    @Query("SELECT f FROM Feedback f WHERE f.author = :author AND" +
            "(:title = '' OR LOWER(f.title) ILIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:description = '' OR LOWER(f.description) ILIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:category = '' OR LOWER(f.category) ILIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(:status = '' OR LOWER(f.status) ILIKE LOWER(CONCAT('%', :status, '%')))")
    Page<Feedback> findAllByFilters(
            @Param("author") User author,
            @Param("title") String title,
            @Param("description") String description,
            @Param("category") String category,
            @Param("status") String status,
            Pageable pageable
    );
}
