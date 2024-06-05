import dao.UserDAO;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final int ID = 15;
    private static final String USERNAME = "username";
    ;
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String FULL_NAME = "fulName";
    private static final String AVATAR = "avatar";
    private User user;
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        user = User.builder()
                .setId(ID)
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .setEmail(EMAIL)
                .setFullName(FULL_NAME)
                .setCreateAt(LocalDateTime.now())
                .setUpdateAt(LocalDateTime.now())
                .setAvatar(AVATAR)
                .build();
    }

    @Test
    void createAccount() {
        User user = new User();
        lenient().when(userDAO.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> byId = userService.findById(user.getId());
        assertNotNull(byId);

    }

    @Test
    void removeAccount() {
        userService.removeAccount(user);
        Mockito.verify(userDAO).remove(user.getId());
    }

    @Test
    void findByUserName() {
        Optional<User> expectedUser = Optional.of(user);
        when(userDAO.findByUsername(user.getUsername())).thenReturn(expectedUser);
        Optional<User> actualUser = userService.findByUserName(user.getUsername());
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findById() {
        Optional<User> expectedUser = Optional.of(user);
        when(userDAO.findById(user.getId())).thenReturn(expectedUser);
        Optional<User> actualUser = userService.findById(user.getId());
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("Test for find all")
    void findAll() {
        List<User> expectedUsers = new ArrayList<>();
        when(userDAO.findAll()).thenReturn(expectedUsers);
        List<User> actualUsers = userService.findAll();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void updatePasswordById() {
        String password = "password";
        long id = 1L;
        Optional<User> expectedUser = Optional.of(new User());
        when(userDAO.updatePasswordById(password, id)).thenReturn(expectedUser);
        Optional<User> actualUser = userService.updatePasswordById(password, id);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void updateById() {
        User user = new User();
        Optional<User> expectedUser = Optional.of(new User());
        when(userDAO.updateById(user)).thenReturn(expectedUser);
        Optional<User> actualUser = userService.updateById(user);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserWithPosts() {
        String username = "username";
        Optional<User> expectedUser = Optional.of(new User());
        when(userDAO.findUserWithPosts(username)).thenReturn(expectedUser);
        Optional<User> actualUser = userService.getUserWithPosts(username);
        assertEquals(expectedUser, actualUser);
    }
}
