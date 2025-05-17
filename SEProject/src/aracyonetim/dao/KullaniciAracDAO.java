package aracyonetim.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Kullanici-Arac ilişki tablosu için veritabanı erişim ve işlemlerini yöneten DAO sınıfı
 */
public class KullaniciAracDAO {
    private Connection connection;

    /**
     * Veritabanı bağlantısı ile DAO nesnesini oluşturur
     * @param connection Aktif veritabanı bağlantısı
     */
    public KullaniciAracDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Kullanıcıya araç atar
     * @param kullaniciId Kullanıcı ID'si
     * @param aracId Araç ID'si
     * @return İşlem başarılı ise true
     * @throws SQLException Veritabanı hatası durumunda
     */
    public boolean kullaniciAracAta(int kullaniciId, int aracId) throws SQLException {
        // Önce aracın başka bir kullanıcıya aktif olarak atanmadığından emin ol
        String checkSql = "SELECT COUNT(*) FROM KullaniciArac WHERE aracId = ? AND aktif = 1";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, aracId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // Araç zaten bir kullanıcıya atanmış, önce bu atamayı pasif yap
                String deactivateSql = "UPDATE KullaniciArac SET aktif = 0 WHERE aracId = ? AND aktif = 1";
                try (PreparedStatement deactivateStmt = connection.prepareStatement(deactivateSql)) {
                    deactivateStmt.setInt(1, aracId);
                    deactivateStmt.executeUpdate();
                }
            }
        }

        // Yeni atama kaydı ekle
        String sql = "INSERT INTO KullaniciArac (kullaniciId, aracId, atamaTarihi, aktif) VALUES (?, ?, ?, 1)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, kullaniciId);
            stmt.setInt(2, aracId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Kullanıcıdan araç atamasını kaldırır
     * @param kullaniciId Kullanıcı ID'si
     * @param aracId Araç ID'si
     * @return İşlem başarılı ise true
     * @throws SQLException Veritabanı hatası durumunda
     */
    public boolean kullaniciAracAtamaKaldir(int kullaniciId, int aracId) throws SQLException {
        String sql = "UPDATE KullaniciArac SET aktif = 0 WHERE kullaniciId = ? AND aracId = ? AND aktif = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, kullaniciId);
            stmt.setInt(2, aracId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Bir kullanıcıya atanmış tüm aktif araçların ID'lerini getirir
     * @param kullaniciId Kullanıcı ID'si
     * @return Araç ID'lerinin listesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    public List<Integer> kullaniciAraclariGetir(int kullaniciId) throws SQLException {
        String sql = "SELECT aracId FROM KullaniciArac WHERE kullaniciId = ? AND aktif = 1";
        List<Integer> aracIds = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, kullaniciId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    aracIds.add(rs.getInt("aracId"));
                }
            }
        }
        
        return aracIds;
    }

    /**
     * Bir araca atanmış kullanıcının ID'sini getirir (varsa)
     * @param aracId Araç ID'si
     * @return Kullanıcı ID'si, eğer atanmamışsa null
     * @throws SQLException Veritabanı hatası durumunda
     */
    public Integer aracKullanicisiGetir(int aracId) throws SQLException {
        String sql = "SELECT kullaniciId FROM KullaniciArac WHERE aracId = ? AND aktif = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, aracId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("kullaniciId");
                }
            }
        }
        
        return null;
    }
} 