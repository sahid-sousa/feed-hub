package br.com.feedhub.unit.application.usecases.comment;

import br.com.feedhub.application.usecases.comment.ListComment;
import br.com.feedhub.interfaces.dto.response.CommentResponse;
import br.com.feedhub.interfaces.dto.response.PageListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ListCommentTest {

    @Mock
    ListComment listComment;

    PageListResponse<CommentResponse> pageListResponse;

    @BeforeEach
    public void setup() {
        //Given
        CommentResponse commentResponse = new CommentResponse(
                1L,
                "Feedback Comment",
                1L,
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        pageListResponse = new PageListResponse<>(
                List.of(commentResponse),
                0,
                10,
                1L,
                1,
                true
        );
    }

    @Test
    @DisplayName("Test Receive Params when ListComment then return PageListResponse")
    void testReceiveParams_whenListComment_thenReturnPageListResponse() {
        //Given
        given(listComment.execute(
                anyLong(),
                anyInt(),
                anyInt(),
                anyString(),
                anyString(),
                any()
        )).willReturn(pageListResponse);

        //When
        PageListResponse<CommentResponse> response = listComment.execute(
                anyLong(),
                anyInt(),
                anyInt(),
                anyString(),
                anyString(),
                any()
        );

        //Then
        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(1, response.totalElements());
        assertEquals(10, response.size());
    }
}