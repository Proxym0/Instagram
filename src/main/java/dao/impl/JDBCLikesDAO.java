package dao.impl;

import dao.LikesDAO;
import entity.Like;
import service.LikeService;
import service.PostService;
import service.UserService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCLikesDAO extends AbstractDAO implements LikesDAO {
    private static final String INSERTING_LIKES = "insert into likes values (default, ?, ?, ?, ?)";
    private static final String DELETION_LIKE_BY_ID = "delete from likes where user_id = ?";
    private static final String SELECTION_BY_ID = "select * from likes where id = ?";
    private static final String SELECTION_ALL_LIKES = "select * from likes";
    private static final String SELECTION_LIKES_BY_POST_ID = "select * from likes where post_id = ?";
    private static final String SELECTION_LIKE_BY_USER_ID = "select * from likes where user_id = ?";
    private static final int ID_COLUMN = 1;
    private static final int USER_ID_COLUMN = 2;
    private static final int POST_ID_COLUMN = 3;
    private static final int CREATE_AT_COLUMN = 4;

    private static JDBCLikesDAO likesDao;

    public static LikesDAO getInstance() {
        if (likesDao == null) {
            likesDao = new JDBCLikesDAO();
        }
        return likesDao;
    }

    @Override
    public void save(Like like) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(INSERTING_LIKES);
            preparedStatement.setLong(1, like.getId());
            preparedStatement.setLong(2, like.getAuthor().getId());
            preparedStatement.setLong(3, like.getPost().getId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(like.getCreateAt()));
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void remove(long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(DELETION_LIKE_BY_ID);
            preparedStatement.setLong(1,id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Like> findById(long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_BY_ID);
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long postId = resultSet.getLong(POST_ID_COLUMN);
            long userId = resultSet.getLong(USER_ID_COLUMN);
            LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
            return Optional.of(Like.builder()
                    .setId(id)
                    .setAuthor(UserService.getInstance().findById(userId).get())
                    .setPost(PostService.getInstance().findPostById(postId).get())
                    .setCreateAt(createAt).setPost(PostService.getInstance().findPostById(postId).get())
                    .build());
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public List<Like> findAll() {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(SELECTION_ALL_LIKES);
            List<Like> likes = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong(ID_COLUMN);
                long userId = resultSet.getLong(USER_ID_COLUMN);
                long postId = resultSet.getLong(POST_ID_COLUMN);
                LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
                likes.add(Like.builder()
                        .setId(id)
                        .setAuthor(UserService.getInstance().findById(userId).get())
                        .setPost(PostService.getInstance().findPostById(postId).get())
                        .setCreateAt(createAt)
                        .build());
            }
            return likes;
        } catch (SQLException ignored) {
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Like> findLikeByUserId(long userId) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_LIKE_BY_USER_ID);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            long id = resultSet.getLong(ID_COLUMN);
            userId = resultSet.getLong(USER_ID_COLUMN);
            long postId = resultSet.getLong(POST_ID_COLUMN);
            LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
            return Optional.of(Like.builder()
                    .setId(id)
                    .setAuthor(LikeService.getInstance().findLikeByUserId(userId).get().getAuthor())
                    .setPost(PostService.getInstance().findPostById(postId).get())
                    .setCreateAt(createAt)
                    .build());

        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    public List<Like> findLikeByPost(long post) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_LIKES_BY_POST_ID);
            preparedStatement.setLong(1, post);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Like> likes = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong(ID_COLUMN);
                long userId = resultSet.getLong(USER_ID_COLUMN);
                LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
                return List.of(Like.builder()
                        .setId(id)
                        .setPost(PostService.getInstance().findPostById(post).get())
                        .setAuthor(UserService.getInstance().findById(userId).get())
                        .setCreateAt(createAt)
                        .build());
            } return likes;
        } catch (SQLException ignored) {

        }
        return new ArrayList<>();
    }
}
