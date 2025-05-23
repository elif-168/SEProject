import aracyonetim.db.DatabaseRestorer;

public class RestoreDatabase {
    public static void main(String[] args) {
        System.out.println("Starting database restoration...");
        DatabaseRestorer.restoreDatabase();
        System.out.println("Database restoration completed. You should now be able to log in.");
    }
} 