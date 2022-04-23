package kib.lab7.server.db_utils;

import java.sql.Connection;
import java.sql.SQLException;

public class TablesCreator {

    public void initializeTables() throws SQLException {
        DBConnector dbConnector = new DBConnector();
        Connection conn = dbConnector.getConnection();
    }

}
