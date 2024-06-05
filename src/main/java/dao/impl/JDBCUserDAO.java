package dao.impl;
import java.sql.*;

import dao.UserDAO;
import entity.Post;
import entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCUserDAO extends AbstractDAO implements UserDAO {
    private static final int ID_COLUMN = 1;
    private static final int USERNAME_COLUMN = 2;
    private static final int PASSWORD_COLUMN = 3;
    private static final int EMAIL_COLUMN = 4;
    private static final int FULL_NAME_COLUMN = 5;
    private static final int CREATE_AT_COLUMN = 6;
    private static final int UPDATE_AT_COLUMN = 7;
    private static final int AVATAR_COLUMN = 8;
    private static final String INSERTING = "insert into users values (default, ?, ?, ?, ?, ?, ?,?)";
    private static final String DELETION_BY_ID = "delete from users where id = ?";
    private static final String SELECTION_BY_USERNAME = "select * from users where username = ?";
    private static final String SELECTION_BY_ID = "select * from users where id = ?";
    private static final String SELECTION_ALL = "select * from users";
    private static final String UPDATED_PASSWORD = "update users\n" +
            "set password  = ?, " +
            "    update_at = ? " +
            "where id = ? " +
            "returning *";
    private static final String UPDATED_EMAIL_FULL_NAME_AVATAR = "update users\n" +
            "set email     = ?, " +
            "    fullname  = ?, " +
            "    avatar    = ?, " +
            "    update_at = ? " +
            "where id = ? " +
            "returning *";
    private static final String SELECTION_USER_WITH_POSTS = "select posts.id," +
            "posts.url," +
            "posts.description," +
            "posts.create_at, " +
            "users.id," +
            "users.username," +
            "users.avatar " +
            "from posts " +
            "inner join users on posts.user_id = users.id " +
            "where username = ?";
    private static JDBCUserDAO userDAO;

    public static JDBCUserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new JDBCUserDAO();
        }
        return userDAO;
    }

    private JDBCUserDAO() {
    }

    @Override
    public void save(User user) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(INSERTING);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getFullName());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(user.getCreateAt()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(user.getUpdateAt()));
            preparedStatement.setString(7, user.getAvatar());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(DELETION_BY_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.of(getUser(resultSet));
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(SELECTION_ALL);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(getUser(resultSet));
            }
            return users;
        } catch (SQLException ignored) {
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_BY_USERNAME);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.of(getUser(resultSet));
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updatePasswordById(String password, long id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATED_PASSWORD);
            preparedStatement.setString(1, password);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setLong(3, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.of(getUser(resultSet));
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updateById(User user) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATED_EMAIL_FULL_NAME_AVATAR);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFullName());
            preparedStatement.setString(3, user.getAvatar());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(user.getUpdateAt()));
            preparedStatement.setLong(5, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.of(getUser(resultSet));
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserWithPosts(String username) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECTION_USER_WITH_POSTS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return Optional.of(getUserWithPosts(resultSet));
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    private User getUserWithPosts(ResultSet resultSet) throws SQLException {
        List<Post> posts = new ArrayList<>();
        resultSet.afterLast();
        while (resultSet.previous()) {
            long postId = resultSet.getLong(1);
            String url = resultSet.getString(2);
            String description = resultSet.getString(3);
            LocalDateTime postCreateAt = resultSet.getTimestamp(4).toLocalDateTime();
            posts.add(Post.builder()
                    .setId(postId)
                    .setUrl(url)
                    .setDescription(description)
                    .setCreateAt(postCreateAt)
                    .build());
        }
        resultSet.absolute(1);
        long userId = resultSet.getLong(5);
        String username = resultSet.getString(6);
        String avatar = resultSet.getString(7);
        return User.builder()
                .setId(userId)
                .setUsername(username)
                .setAvatar(avatar)
                .setPosts(posts)
                .build();
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(ID_COLUMN);
        String username = resultSet.getString(USERNAME_COLUMN);
        String password = resultSet.getString(PASSWORD_COLUMN);
        String email = resultSet.getString(EMAIL_COLUMN);
        String fullName = resultSet.getString(FULL_NAME_COLUMN);
        LocalDateTime createAt = resultSet.getTimestamp(CREATE_AT_COLUMN).toLocalDateTime();
        LocalDateTime updateAt = resultSet.getTimestamp(UPDATE_AT_COLUMN).toLocalDateTime();
        String avatar = resultSet.getString(AVATAR_COLUMN);
        return User.builder()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setEmail(email)
                .setFullName(fullName)
                .setCreateAt(createAt)
                .setUpdateAt(updateAt)
                .setAvatar(avatar)
                .build();
    }
}
