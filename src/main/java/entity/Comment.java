package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment extends AbstractEntity{
    private String body;
    private User author;
    private Post post;

    public Comment() {
    }
    public static CommentBuilder builder() {
        return new Comment().new CommentBuilder();
    }
    public class CommentBuilder{
        public CommentBuilder() {
        }
        public CommentBuilder setId (long id){
            Comment.this.id = id;
            return this;
        }
        public CommentBuilder setCreateAt (LocalDateTime createAt){
            Comment.this.createAt = createAt;
            return this;
        }
        public CommentBuilder setBody (String body){
            Comment.this.body = body;
            return this;
        }
        public CommentBuilder setAuthor(User author){
            Comment.this.author = author;
            return this;
        }
        public CommentBuilder setPost (Post post){
            Comment.this.post = post;
            return this;
        }
        public Comment build() {
            return Comment.this;
        }
    }

    public String getBody() {
        return body;
    }

    public User getAuthor() {
        return author;
    }

    public Post getPost() {
        return post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(body, comment.body) && Objects.equals(author, comment.author) && Objects.equals(post, comment.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, author, post);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "body='" + body + '\'' +
                ", author=" + author +
                ", post=" + post +
                ", id=" + id +
                ", createAt=" + createAt +
                '}';
    }
}

