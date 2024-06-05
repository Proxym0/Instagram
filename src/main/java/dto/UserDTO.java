package dto;

import entity.AbstractEntity;
import entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class UserDTO extends AbstractEntity {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private List<Post> posts;
    private LocalDateTime updateAt;
    private String avatar;

    public UserDTO() {
    }

    public static UserBuilder builder() {
        return new UserDTO().new UserBuilder();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO user = (UserDTO) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(fullName, user.fullName) && Objects.equals(posts, user.posts) && Objects.equals(updateAt, user.updateAt) && Objects.equals(avatar, user.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, fullName, posts, updateAt, avatar);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", posts=" + posts +
                ", updateAt=" + updateAt +
                ", id=" + id +
                ", createAt=" + createAt +
                '}';
    }

    public class UserBuilder {
        public UserBuilder() {
        }

        public UserDTO.UserBuilder setId(long id) {
            UserDTO.this.id = id;
            return this;
        }

        public UserBuilder setCreateAt(LocalDateTime createAt) {
            UserDTO.this.createAt = createAt;
            return this;
        }

        public UserBuilder setUsername(String username) {
            UserDTO.this.username = username;
            return this;
        }

        public UserBuilder setPassword(String password) {
            UserDTO.this.password = password;
            return this;
        }

        public UserBuilder setEmail(String email) {
            UserDTO.this.email = email;
            return this;
        }

        public UserBuilder setFullName(String fullName) {
            UserDTO.this.fullName = fullName;
            return this;
        }

        public UserBuilder setPosts(List<Post> posts) {
            UserDTO.this.posts = posts;
            return this;
        }

        public UserBuilder setUpdateAt(LocalDateTime updateAt) {
            UserDTO.this.updateAt = updateAt;
            return this;
        }

        public UserBuilder setAvatar(String avatar) {
            UserDTO.this.avatar = avatar;
            return this;
        }

        public UserDTO build() {
            return UserDTO.this;
        }
    }
}

