package aracyonetim.db;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static Connection connection;
    private static final String DB_FOLDER = "database";
    private static final String DB_NAME = "aracyonetim.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FOLDER + "/" + DB_NAME;

    public static Connection getConnection() throws SQLException {

        Connection conn = null;
            try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("SQLite database connected at: " + Paths.get(DB_FOLDER, DB_NAME).toAbsolutePath());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
            return conn;
    }

}






