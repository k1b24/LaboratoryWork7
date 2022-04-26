package kib.lab7.server.db_utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    public Connection getConnection() throws SQLException {
        String login = System.getenv("DB_LOGIN");
        String password = System.getenv("DB_PASS");
        String host = System.getenv("DB_HOST");
        String name = System.getenv("DB_NAME");
        String dbHost = "jdbc:postgresql://" + host + ":5432/" + name;
        return DriverManager.getConnection(dbHost, login, password);
    }
}
