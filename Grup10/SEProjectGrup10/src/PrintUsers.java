import aracyonetim.db.DBConnection;
import aracyonetim.model.Kullanici;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PrintUsers {
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to database...");
            Connection conn = DBConnection.getConnection();
            System.out.println("Connection successful!");
            
            System.out.println("\nUsers in the database:");
            System.out.println("--------------------------------------------------");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Kullanici");
            
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("kullaniciId"));
                System.out.println("Ad: " + rs.getString("ad"));
                System.out.println("Soyad: " + rs.getString("soyad"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Şifre: " + rs.getString("sifre"));
                System.out.println("Rol: " + rs.getString("rol"));
                System.out.println("Firma: " + rs.getString("firma"));
                System.out.println("--------------------------------------------------");
            }
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No users found in the database!");
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 