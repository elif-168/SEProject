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
        String sql = "INSERT INTO Kullanici (ad, soyad, kullaniciAdi, sifre, email, telefon, rol, departman, firmaBilgisi, aktif) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kullanici.getAd());
            stmt.setString(2, kullanici.getSoyad());
            stmt.setString(3, kullanici.getKullaniciAdi());
            stmt.setString(4, kullanici.getSifre());
            stmt.setString(5, kullanici.getEmail());
            stmt.setString(6, kullanici.getTelefon());
            stmt.setString(7, kullanici.getRol());
            stmt.setString(8, kullanici.getDepartman());
            stmt.setString(9, kullanici.getFirmaBilgisi());
            stmt.setBoolean(10, kullanici.isAktif());
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
        String sql = "UPDATE Kullanici SET ad=?, soyad=?, kullaniciAdi=?, sifre=?, email=?, telefon=?, rol=?, departman=?, firmaBilgisi=?, aktif=? " +
                "WHERE kullaniciId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kullanici.getAd());
            stmt.setString(2, kullanici.getSoyad());
            stmt.setString(3, kullanici.getKullaniciAdi());
            stmt.setString(4, kullanici.getSifre());
            stmt.setString(5, kullanici.getEmail());
            stmt.setString(6, kullanici.getTelefon());
            stmt.setString(7, kullanici.getRol());
            stmt.setString(8, kullanici.getDepartman());
            stmt.setString(9, kullanici.getFirmaBilgisi());
            stmt.setBoolean(10, kullanici.isAktif());
            stmt.setInt(11, kullanici.getKullaniciId());
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

    private Kullanici mapResultSetToKullanici(ResultSet rs) throws SQLException {
        Kullanici k = new Kullanici();
        k.setKullaniciId(rs.getInt("kullaniciId"));
        k.setAd(rs.getString("ad"));
        k.setSoyad(rs.getString("soyad"));
        k.setKullaniciAdi(rs.getString("kullaniciAdi"));
        k.setSifre(rs.getString("sifre"));
        k.setEmail(rs.getString("email"));
        k.setTelefon(rs.getString("telefon"));
        k.setRol(rs.getString("rol"));
        k.setDepartman(rs.getString("departman"));
        k.setFirmaBilgisi(rs.getString("firmaBilgisi"));
        k.setAktif(rs.getBoolean("aktif"));
        k.setOlusturmaTarihi(rs.getTimestamp("olusturmaTarihi"));
        return k;
    }
}
