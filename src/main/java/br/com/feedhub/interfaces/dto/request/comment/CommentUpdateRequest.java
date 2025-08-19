package br.com.feedhub.interfaces.dto.request.comment;

public class CommentUpdateRequest extends CommentDetails {

    private Long id;

    public CommentUpdateRequest() {}

    public CommentUpdateRequest(Long id, String content) {
        super(content);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
