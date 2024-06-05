package service;

import dao.UserDAO;
import dao.impl.JDBCUserDAO;
import dto.UserDTO;
import entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public  class UserService {

    private static UserService userService;
    private  UserDAO userDAO = JDBCUserDAO.getInstance();

    private UserService() {
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public void createAccount(User user) {
        userDAO.save(user);
    }

    public void removeAccount(User user) {
        userDAO.remove(user.getId());
    }

    public Optional<User> findByUserName(String username) {
        return userDAO.findByUsername(username);
    }

    public Optional<User> findById(long id) {
        return userDAO.findById(id);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public Optional<User> updatePasswordById(String password, long id) {
        return userDAO.updatePasswordById(password, id);
    }

    public Optional<User> updateById(User user){
        return userDAO.updateById(user);
    }

    public Optional<User> getUserWithPosts(String username){
        return userDAO.findUserWithPosts(username);
    }
    public User toEntity(UserDTO userDTO){
       return User.builder()
                .setUsername(userDTO.getUsername())
                .setPassword(userDTO.getPassword())
                .setEmail(userDTO.getEmail())
                .setFullName(userDTO.getFullName())
                .setCreateAt(LocalDateTime.now())
                .setUpdateAt(LocalDateTime.now())
                .setAvatar(userDTO.getAvatar())
                .build();
    }
}
