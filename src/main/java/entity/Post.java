package entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Post extends AbstractEntity {
    private String description;
    private String url;
    private User creator;
    private List<Comment> comments;
    private List<Like> likes;

    public Post() {
    }

    public static PostBuilder builder() {
        return new Post().new PostBuilder();
    }

    public class PostBuilder {
        public PostBuilder() {
        }

        public PostBuilder setId(long id) {
            Post.this.id = id;
            return this;
        }
        public PostBuilder setCreateAt(LocalDateTime createAt){
            Post.this.createAt = createAt;
            return this;
        }
        public PostBuilder setDescription(String description){
            Post.this.description = description;
            return this;
        }
        public PostBuilder setUrl(String url){
            Post.this.url = url;
            return this;
        }
        public PostBuilder setCreator(User creator){
            Post.this.creator = creator;
            return this;
        }
        public PostBuilder setComments(List<Comment> comments){
            Post.this.comments = comments;
            return this;
        }
        public PostBuilder setLikes (List<Like> likes){
            Post.this.likes = likes;
            return this;
        }
        public Post build(){
            return Post.this;
        }
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public User getCreator() {
        return creator;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(description, post.description) && Objects.equals(url, post.url) && Objects.equals(creator, post.creator) && Objects.equals(comments, post.comments) && Objects.equals(likes, post.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, url, creator, comments, likes);
    }

    @Override
    public String toString() {
        return "Post{" +
                "description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", creator=" + creator +
                ", comments=" + comments +
                ", likes=" + likes +
                ", id=" + id +
                ", createAt=" + createAt +
                '}';
    }
}
