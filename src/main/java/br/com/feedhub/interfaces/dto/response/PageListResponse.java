package br.com.feedhub.interfaces.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageListResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static <T> PageListResponse<T> of(Page<T> page) {
        return new PageListResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
