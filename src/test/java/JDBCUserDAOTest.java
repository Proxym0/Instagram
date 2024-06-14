import dao.impl.JDBCUserDAO;
import entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertThat;

@Testcontainers
class JDBCUserDAOTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.2");

    private static JDBCUserDAO userDAO;

    @BeforeAll
    static void setUp() throws SQLException {
        userDAO = JDBCUserDAO.getInstance();
        userDAO.setConnection(postgreSQLContainer.createConnection());
    }

    @AfterAll
    static void tearDown() {
        userDAO.closeConnection();
    }

    @Test
    void save_shouldPersistUser() {
        // Given
        User user = new User("username", "password", "email", "FullName", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "avatar");

        // When
        userDAO.save(user);

        // Then
        Optional<User> savedUser = userDAO.findById(1L);
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get()).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void findById_shouldFindUserById() {
        // Given
        User user = new User("username", "password", "email", "FullName", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "avatar");
        userDAO.save(user);

        // When
        Optional<User> foundUser = userDAO.findById(1L);

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void remove_shouldDeleteUser() {
        // Given
        User user = new User("username", "password", "email", "FullName", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "avatar");
        userDAO.save(user);

        // When
        userDAO.remove(1L);

        // Then
        Optional<User> deletedUser = userDAO.findById(1L);
        assertThat(deletedUser).isEmpty();
    }
}
