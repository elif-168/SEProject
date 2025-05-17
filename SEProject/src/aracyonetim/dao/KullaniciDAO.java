package aracyonetim.dao;

import aracyonetim.model.Kullanici;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KullaniciDAO {
    private Connection connection;

    public KullaniciDAO(Connection connection) {
        this.connection = connection;
    }

    public void kullaniciEkle(Kullanici kullanici) throws SQLException {
        String sql = "INSERT INTO Kullanici (ad, soyad, email, telefon, firma, rol, sifre, aktif) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kullanici.getAd());
            stmt.setString(2, kullanici.getSoyad());
            stmt.setString(3, kullanici.getEmail());
            stmt.setString(4, kullanici.getTelefon());
            stmt.setString(5, kullanici.getFirma());
            stmt.setString(6, kullanici.getRol());
            stmt.setString(7, kullanici.getSifre());
            stmt.setBoolean(8, kullanici.isAktif());
            stmt.executeUpdate();
        }
    }

    public Kullanici kullaniciGetirById(int id) throws SQLException {
        String sql = "SELECT * FROM Kullanici WHERE kullaniciId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToKullanici(rs);
            }
        }
        return null;
    }

    public Kullanici kullaniciGetirByName(String kullaniciAdi) throws SQLException {
        String sql = "SELECT * FROM Kullanici WHERE kullaniciAdi = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kullaniciAdi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToKullanici(rs);
            }
        }
        return null;
    }

    public List<Kullanici> firmadakiKullanicilariGetir(String firma) throws SQLException {
        List<Kullanici> liste = new ArrayList<>();
        String sql = "SELECT * FROM Kullanici WHERE firma = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, firma);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                liste.add(mapResultSetToKullanici(rs));
            }

        }
        return liste;
    }

    public List<Kullanici> tumKullanicilariGetir() throws SQLException {
        List<Kullanici> liste = new ArrayList<>();
        String sql = "SELECT * FROM Kullanici";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(mapResultSetToKullanici(rs));
            }
        }
        return liste;
    }

    public void kullaniciGuncelle(Kullanici kullanici) throws SQLException {
        String sql = "UPDATE Kullanici SET ad=?, soyad=?, email=?, telefon=?, firma=?, rol=?, sifre=?, aktif=? " +
                "WHERE kullaniciId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kullanici.getAd());
            stmt.setString(2, kullanici.getSoyad());
            stmt.setString(3, kullanici.getEmail());
            stmt.setString(4, kullanici.getTelefon());
            stmt.setString(5, kullanici.getFirma());
            stmt.setString(6, kullanici.getRol());
            stmt.setString(7, kullanici.getSifre());
            stmt.setBoolean(8, kullanici.isAktif());
            stmt.setInt(9, kullanici.getKullaniciId());
            stmt.executeUpdate();
        }
    }

    public void kullaniciSil(int id) throws SQLException {
        String sql = "DELETE FROM Kullanici WHERE kullaniciId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Kullanıcı giriş kontrolü yapar
     * @param email Kullanıcı email
     * @param sifre Kullanıcı şifresi
     * @return Giriş başarılıysa Kullanici nesnesi, değilse null
     * @throws SQLException Veritabanı hatası durumunda
     */
    public Kullanici kullaniciGiris(String email, String sifre) throws SQLException {
        System.out.println("KullaniciDAO.kullaniciGiris: Querying with email=" + email + ", sifre=" + sifre);
        
        // For debugging - Check if this exact user exists
        try (Statement checkStmt = connection.createStatement();
             ResultSet checkRs = checkStmt.executeQuery("SELECT * FROM Kullanici WHERE email = '" + email + "'")) {
            
            if (checkRs.next()) {
                System.out.println("Found a user with this email. Password in DB: " + checkRs.getString("sifre"));
            } else {
                System.out.println("No user found with email: " + email);
            }
        }
        
        String sql = "SELECT * FROM Kullanici WHERE email = ? AND sifre = ? AND aktif = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, sifre);
            
            System.out.println("Executing SQL: " + sql.replace("?", "'" + email + "','"+sifre+"'"));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User found, mapping result set to Kullanici object");
                    return mapResultSetToKullanici(rs);
                } else {
                    System.out.println("No matching user found with email/password combination");
                    return null;
                }
            }
        }
    }

    private Kullanici mapResultSetToKullanici(ResultSet rs) throws SQLException {
        Kullanici k = new Kullanici();
        
        k.setKullaniciId(rs.getInt("kullaniciId"));
        k.setAd(rs.getString("ad"));
        k.setSoyad(rs.getString("soyad"));
        
        // Handle optional fields
        try {
            k.setKullaniciAdi(rs.getString("kullaniciAdi"));
        } catch (SQLException e) {
            k.setKullaniciAdi(null);
        }
        
        k.setSifre(rs.getString("sifre"));
        k.setEmail(rs.getString("email"));
        k.setTelefon(rs.getString("telefon"));
        k.setRol(rs.getString("rol"));
        
        try {
            k.setDepartman(rs.getString("departman"));
        } catch (SQLException e) {
            k.setDepartman(null);
        }
        
        k.setFirma(rs.getString("firma"));
        
        try {
            k.setFirmaBilgisi(rs.getString("firmaBilgisi"));
        } catch (SQLException e) {
            // If firmaBilgisi doesn't exist, set it to the same as firma
            k.setFirmaBilgisi(rs.getString("firma"));
        }
        
        k.setAktif(rs.getBoolean("aktif"));
        
        try {
            k.setOlusturmaTarihi(rs.getTimestamp("olusturmaTarihi"));
        } catch (SQLException e) {
            k.setOlusturmaTarihi(null);
        }
        
        return k;
    }
}
