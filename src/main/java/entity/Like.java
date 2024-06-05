package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Like extends AbstractEntity {
    private User author;
    private Post post;

    public Like() {
    }

    public static LikeBuilder builder() {
        return new Like().new LikeBuilder();
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
        Like like = (Like) o;
        return Objects.equals(author, like.author) && Objects.equals(post, like.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, post);
    }

    @Override
    public String toString() {
        return "Like{" +
                "author=" + author +
                ", post=" + post +
                ", id=" + id +
                ", createAt=" + createAt +
                '}';
    }

    public class LikeBuilder {
        public LikeBuilder() {
        }

        public LikeBuilder setId(long id) {
            Like.this.id = id;
            return this;
        }

        public LikeBuilder setCreateAt(LocalDateTime createAt) {
            Like.this.createAt = createAt;
            return this;
        }

        public LikeBuilder setAuthor(User author) {
            Like.this.author = author;
            return this;
        }

        public LikeBuilder setPost(Post post) {
            Like.this.post = post;
            return this;
        }

        public Like build() {
            return Like.this;
        }
    }
}
