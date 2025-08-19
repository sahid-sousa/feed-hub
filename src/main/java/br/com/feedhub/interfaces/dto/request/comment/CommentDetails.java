package br.com.feedhub.interfaces.dto.request.comment;

public abstract class CommentDetails {

    private String content;

    public CommentDetails() {}

    public CommentDetails(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
