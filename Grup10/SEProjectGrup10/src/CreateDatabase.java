import aracyonetim.db.DatabaseInitializer;

public class CreateDatabase {
    public static void main(String[] args) {
        System.out.println("Initializing database...");
        DatabaseInitializer.initializeDatabase();
        System.out.println("Database initialization completed.");
    }
} 