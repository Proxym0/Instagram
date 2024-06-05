package dao.impl;

import dao.PostDAO;
import entity.Comment;
import entity.Post;
import entity.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPostDAO extends AbstractDAO implements PostDAO {
    private static final String INSERTING_POST = "insert into posts values (default, ?, ?, ?, ?)";
    private static final String DELETION_POST_BY_ID = "delete from posts where id = ?";
    private static final String SELECTION_BY_ID = "select * from posts where id = ?";
    private static final String SELECTION_ALL_POSTS = "select * from posts";
    private static final String SELECTION_POSTS_BY_USERID = "select * from posts where user_id=?";

    private static final String SELECTION_POSTS_WITH_COMMENTS = "select comments.id, " +
            "       comments.body, " +
            "       comments.create_at, " +
            "       users.id, " +
            "       users.username, " +
            "       users.avatar, " +
            "       posts.id, " +
            "       posts.url, " +
            "       posts.description " +
            "from comments " +
            "         inner join users on comments.user_id = users.id " +
            "         inner join posts on comments.post_id = posts.id " +
            "where posts.id = ?";
    private static final int ID_COLUMN = 1;
    private static final int DESCRIPTION_COLUMN = 2;
    private static final int URL_COLUMN = 3;
    private static final int USER_ID_COLUMN = 4;
    private static final int CREATE_AT_COLUMN = 5;
    private static JDBCPostDAO postDAO;

    public JDBCPostDAO() {
    }

    public static JDBCPostDAO getInstance() {
        if (postDAO == null) {
            postDAO = new JDBCPostDAO();
        }
        return postDAO;
    }

    @Override
    public void save(Post post) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(INSERTING_POST);
            preparedStatement.setString(1, post.getDescription());
            preparedStatement.setString(2, post.getUrl());
            preparedStatement.setLong(3, post.getCreator().getId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(post.getCreateAt()));
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(DELETION_POST_BY_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Post> findById(long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.of(getPost(resultSet));
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    public List<Post> findPostsByUserId(long userid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_POSTS_BY_USERID);
            preparedStatement.setLong(1, userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> posts = new ArrayList<>();
            while (resultSet.next()) {
                posts.add(getPost(resultSet));
            }
            return posts;

        } catch (SQLException ignored) {

        }
        return new ArrayList<>();
    }

    @Override
    public List<Post> findAll() {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(SELECTION_ALL_POSTS);
            List<Post> posts = new ArrayList<>();
            while (resultSet.next()) {
                posts.add(getPost(resultSet));
            }
            return posts;
        } catch (SQLException ignored) {
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Post> findPostWithComments(long postId) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_POSTS_WITH_COMMENTS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return Optional.of(getPostWithComments(resultSet));
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    private Post getPostWithComments(ResultSet resultSet) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        resultSet.afterLast();
        while (resultSet.previous()) {
            long commentId = resultSet.getLong(1);
            String body = resultSet.getString(2);
            LocalDateTime createAt = resultSet.getTimestamp(3).toLocalDateTime();
            long userId = resultSet.getLong(4);
            String username = resultSet.getString(5);
            String avatar = resultSet.getString(6);
            comments.add(Comment.builder()
                    .setId(commentId)
                    .setBody(body)
                    .setCreateAt(createAt)
                    .setAuthor(User.builder()
                            .setId(userId)
                            .setUsername(username)
                            .setAvatar(avatar)
                            .build())
                    .build());
        }
        resultSet.absolute(1);
        long postId = resultSet.getLong(7);
        String url = resultSet.getString(8);
        String description = resultSet.getString(9);
        return Post.builder()
                .setId(postId)
                .setUrl(url)
                .setDescription(description)
                .setComments(comments)
                .build();
    }


    private Post getPost(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(ID_COLUMN);
        String description = resultSet.getString(DESCRIPTION_COLUMN);
        String url = resultSet.getString(URL_COLUMN);
        LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
        return Post.builder()
                .setId(id)
                .setDescription(description)
                .setUrl(url)
                .setCreateAt(createAt)
                .build();
    }
}
