package dao.impl;

import dao.CommentDAO;
import entity.Comment;
import service.PostService;
import service.UserService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCCommentDAO extends AbstractDAO implements CommentDAO {
    private static final String INSERTING_COMMENT = "insert into comments values (default, ?, ?, ?, ?)";
    private static final String DELETION_COMMENT_BY_ID = "delete from comments where id = ?";
    private static final String SELECTION_COMMENT_BY_ID = "select * from comments where id = ?";
    private static final String SELECTION_ALL_COMMENTS = "select * from comments";
    private static final String SELECTION_ALL_COMMENTS_BY_POST_ID = "select * from comments where post_id = ?";
    private static final int ID_COLUMN = 1;
    private static final int BODY_COLUMN = 2;
    private static final int USER_ID_COLUMN = 3;
    private static final int CREATE_AT_COLUMN = 4;
    private static final int POST_ID_COLUMN = 5;
    private static JDBCCommentDAO commentDAO;

    private JDBCCommentDAO() {

    }

    public static JDBCCommentDAO getInstance() {
        if (commentDAO == null) {
            commentDAO = new JDBCCommentDAO();
        }
        return commentDAO;
    }

    @Override
    public void save(Comment comment) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(INSERTING_COMMENT);
            preparedStatement.setString(1, comment.getBody());
            preparedStatement.setLong(2, comment.getAuthor().getId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(comment.getCreateAt()));
            preparedStatement.setLong(4, comment.getAuthor().getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(DELETION_COMMENT_BY_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Comment> findById(long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_COMMENT_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String body = resultSet.getString(BODY_COLUMN);
            long userId = resultSet.getLong(USER_ID_COLUMN);
            LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
            long postId = resultSet.getLong(POST_ID_COLUMN);
            return Optional.of(Comment.builder()
                    .setId(id)
                    .setBody(body)
                    .setAuthor(UserService.getInstance().findById(userId).get())
                    .setCreateAt(createAt).setPost(PostService.getInstance().findPostById(postId).get())
                    .build());
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public List<Comment> findAll() {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(SELECTION_ALL_COMMENTS);
            List<Comment> comments = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong(ID_COLUMN);
                String body = resultSet.getString(BODY_COLUMN);
                long userId = resultSet.getLong(USER_ID_COLUMN);
                LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
                long postId = resultSet.getLong(POST_ID_COLUMN);
                comments.add(Comment.builder()
                        .setId(id)
                        .setBody(body)
                        .setAuthor(UserService.getInstance().findById(userId).get())
                        .setPost(PostService.getInstance().findPostById(postId).get())
                        .setCreateAt(createAt)
                        .build());
            }
            return comments;
        } catch (SQLException ignored) {
        }
        return new ArrayList<>();
    }

    @Override
    public List<Comment> findAllCommentsByPostId(long postId) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_ALL_COMMENTS_BY_POST_ID);
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Comment> comments = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong(ID_COLUMN);
                String body = resultSet.getString(BODY_COLUMN);
                long userId = resultSet.getLong(USER_ID_COLUMN);
                LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
                comments.add(Comment.builder()
                        .setId(id)
                        .setBody(body)
                        .setAuthor(UserService.getInstance().findById(userId).get())
                        .setPost(PostService.getInstance().findPostById(postId).get())
                        .setCreateAt(createAt)
                        .build());
            }
            return comments;
        } catch (SQLException ignored) {
        }
        return new ArrayList<>();
    }
}
