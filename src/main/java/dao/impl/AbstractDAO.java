package dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDAO {
    public static final String URL = "jdbc:postgresql://localhost:5432/instagram_db";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    private final Connection connection;

    public AbstractDAO() {
        this.connection = createConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    public Connection createConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
