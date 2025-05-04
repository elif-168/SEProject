import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static Connection connection;
    private static final String DB_FOLDER = "database";
    private static final String DB_NAME = "application.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FOLDER + "/" + DB_NAME;

    public static Connection getConnection() throws SQLException {

        Connection conn = null;
            try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("SQLite database created at: " + Paths.get(DB_FOLDER, DB_NAME).toAbsolutePath());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
            return conn;
    }

    public static void initializeDB() {
        String sql = "CREATE TABLE IF NOT EXISTS emp_login (" +
                "emp_id INTEGER PRIMARY KEY," +
                "emp_password varchar(45) NOT NULL);";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database tables initialized");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}






