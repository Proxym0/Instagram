package dao;

import entity.User;

import java.util.Optional;

public interface UserDAO extends DAO<User> {
    Optional<User> findByUsername(String username);

    Optional<User> updatePasswordById(String password, long id);

    Optional<User> updateById(User user);

    Optional<User> findUserWithPosts(String username);
}
