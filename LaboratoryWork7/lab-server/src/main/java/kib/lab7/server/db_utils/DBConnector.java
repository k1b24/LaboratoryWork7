package kib.lab7.server.db_utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String LOGIN = System.getenv("DB_LOGIN");
    private static final String PASSWORD = System.getenv("DB_PASS");
    private static final String HOST = System.getenv("DB_HOST");
    private static final String NAME = System.getenv("DB_NAME");
    private static final String DB_HOST = "jdbc:postgresql://" + HOST + ":5432/" + NAME;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_HOST, LOGIN, PASSWORD);
    }
}
