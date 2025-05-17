package aracyonetim.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            // Read SQL script from file system
            String sql = readSqlFile("database/restore_schema.sql");
            
            // Split into individual statements
            String[] statements = sql.split(";");
            
            // Execute each statement
            try (Statement stmt = conn.createStatement()) {
                for (String statement : statements) {
                    if (!statement.trim().isEmpty()) {
                        stmt.execute(statement);
                    }
                }
            }
            
            System.out.println("Database initialized successfully.");
        } catch (SQLException | IOException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String readSqlFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Could not find SQL file: " + file.getAbsolutePath());
        }
        
        return Files.readString(Paths.get(file.getAbsolutePath()));
    }
} 