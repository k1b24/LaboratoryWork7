package kib.lab7.server.db_utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    public Connection getConnection() throws SQLException {
        String login = "postgres";
        String password = "psql"; //todo hardcoded moment, how to solve this???????
        String host = "jdbc:postgresql://localhost:5432/s335106db"; //TODO Поменять как-то эту тему, потому что на гелиосе она может быть другой
        return DriverManager.getConnection(host, login, password);
    }
}
