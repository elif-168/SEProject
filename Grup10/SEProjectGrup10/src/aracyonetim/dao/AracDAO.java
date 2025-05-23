package aracyonetim.dao;

import aracyonetim.model.Arac;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Arac tablosu için veritabanı erişim ve işlemlerini yöneten DAO sınıfı
 */
public class AracDAO {
    private Connection connection;

    /**
     * Veritabanı bağlantısı ile DAO nesnesini oluşturur
     * @param connection Aktif veritabanı bağlantısı
     */
    public AracDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Yeni bir araç kaydı ekler
     * @param arac Eklenecek araç nesnesi
     * @return Eklenen aracın ID'si
     * @throws SQLException Veritabanı hatası durumunda
     */
    public int ekle(Arac arac) throws SQLException {
        String sql = "INSERT INTO Arac (plaka, marka, model, yil, km, kiralik, kiraBaslangicTarihi, " +
                "kiraBitisTarihi, aylikKiraBedeli, firma, aktif) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, arac.getPlaka());
            stmt.setString(2, arac.getMarka());
            stmt.setString(3, arac.getModel());
            stmt.setInt(4, arac.getYil());
            stmt.setInt(5, arac.getKm());
            stmt.setBoolean(6, arac.isKiralik());

            if (arac.getKiraBaslangicTarihi() != null) {
                stmt.setDate(7, Date.valueOf(arac.getKiraBaslangicTarihi()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (arac.getKiraBitisTarihi() != null) {
                stmt.setDate(8, Date.valueOf(arac.getKiraBitisTarihi()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            if (arac.getAylikKiraBedeli() != null) {
                stmt.setBigDecimal(9, arac.getAylikKiraBedeli());
            } else {
                stmt.setNull(9, Types.DECIMAL);
            }

            stmt.setString(10, arac.getFirma());
            stmt.setBoolean(11, arac.isAktif());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Araç ekleme başarısız oldu, etkilenen satır yok");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    arac.setAracId(generatedKeys.getInt(1));
                    return arac.getAracId();
                } else {
                    throw new SQLException("Araç ekleme başarısız oldu, ID alınamadı");
                }
            }
        }
    }

    /**
     * ID'ye göre araç bilgilerini getirir
     * @param aracId Araç ID'si
     * @return Araç nesnesi (Optional olarak)
     * @throws SQLException Veritabanı hatası durumunda
     */
    public Optional<Arac> getirById(int aracId) throws SQLException {
        String sql = "SELECT * FROM Arac WHERE aracId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, aracId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(resultSetToArac(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    /**
     * Plakaya göre araç bilgilerini getirir
     * @param plaka Araç plakası
     * @return Araç nesnesi (Optional olarak)
     * @throws SQLException Veritabanı hatası durumunda
     */
    public Optional<Arac> getirByPlaka(String plaka) throws SQLException {
        String sql = "SELECT * FROM Arac WHERE plaka = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, plaka);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(resultSetToArac(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    /**
     * Tüm aktif araçları listeler
     * @return Araç listesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    public List<Arac> tumAktifAraclariGetir() throws SQLException {
        String sql = "SELECT * FROM Arac WHERE aktif = 1";

        List<Arac> aracList = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                aracList.add(resultSetToArac(rs));
            }
        }

        return aracList;
    }

    /**
     * Belirli bir firmaya ait aktif araçları listeler
     * @param firma Firma adı
     * @return Araç listesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    public List<Arac> firmaAraclariGetir(String firma) throws SQLException {
        String sql = "SELECT a.* FROM Arac a " +
                    "JOIN KullaniciArac ka ON a.aracId = ka.aracId " +
                    "JOIN Kullanici k ON ka.kullaniciId = k.kullaniciId " +
                    "WHERE k.firma = ? AND a.aktif = 1";

        List<Arac> aracList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firma);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    aracList.add(resultSetToArac(rs));
                }
            }
        }

        return aracList;
    }

    /**
     * Kiralanabilir (kiralik=0 ve aktif=1) araçları listeler
     * @return Kiralanabilir araç listesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    public List<Arac> kiralanabilirAraclariGetir() throws SQLException {
        String sql = "SELECT * FROM Arac WHERE kiralik = 0 AND aktif = 1";

        List<Arac> aracList = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                aracList.add(resultSetToArac(rs));
            }
        }

        return aracList;
    }

    /**
     * Araç bilgilerini günceller
     * @param arac Güncellenecek araç nesnesi
     * @return Güncelleme başarılı ise true
     * @throws SQLException Veritabanı hatası durumunda
     */


    /**
     * Aracı kiralık olarak işaretler ve kira bilgilerini ayarlar
     * @param aracId Kiralanacak aracın ID'si
     * @param baslangicTarihi Kira başlangıç tarihi
     * @param bitisTarihi Kira bitiş tarihi
     * @param kiraBedeli Aylık kira bedeli
     * @return İşlem başarılı ise true
     * @throws SQLException Veritabanı hatası durumunda
     */
    public boolean kirala(int aracId, LocalDate baslangicTarihi, LocalDate bitisTarihi, BigDecimal kiraBedeli) throws SQLException {
        String sql = "UPDATE Arac SET kiralik = 1, kiraBaslangicTarihi = ?, kiraBitisTarihi = ?, " +
                "aylikKiraBedeli = ? " +
                "WHERE aracId = ? AND kiralik = 0 AND aktif = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(baslangicTarihi));
            stmt.setDate(2, Date.valueOf(bitisTarihi));
            stmt.setBigDecimal(3, kiraBedeli);
            stmt.setInt(4, aracId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Aracı kiradan çıkarır (kiralik=0 yapar ve kira bilgilerini temizler)
     * @param aracId Kiradan çıkarılacak aracın ID'si
     * @return İşlem başarılı ise true
     * @throws SQLException Veritabanı hatası durumunda
     */
    public boolean kiradanCikar(int aracId) throws SQLException {
        String sql = "UPDATE Arac SET kiralik = 0, kiraBaslangicTarihi = NULL, kiraBitisTarihi = NULL, " +
                "aylikKiraBedeli = NULL " +
                "WHERE aracId = ? AND kiralik = 1 AND aktif = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, aracId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Aracı pasif duruma getirir (siler)
     * @param aracId Pasif yapılacak aracın ID'si
     * @return İşlem başarılı ise true
     * @throws SQLException Veritabanı hatası durumunda
     */
    public boolean pasifYap(int aracId) throws SQLException {
        String sql = "UPDATE Arac SET aktif = 0 WHERE aracId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, aracId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Belirli bir kullanıcıya atanmış araçları getirir
     * @param kullaniciId Kullanıcı ID'si
     * @return Kullanıcıya atanmış araç listesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    public List<Arac> kullanicininAraclari(int kullaniciId) throws SQLException {
        String sql = "SELECT a.* FROM Arac a " +
                     "JOIN KullaniciArac ka ON a.aracId = ka.aracId " +
                     "WHERE ka.kullaniciId = ? AND a.aktif = 1";

        List<Arac> aracList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, kullaniciId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    aracList.add(resultSetToArac(rs));
                }
            }
        }

        return aracList;
    }

    /**
     * Aracın kilometresini günceller
     * @param aracId Araç ID'si
     * @param km Yeni kilometre değeri
     * @return İşlem başarılı ise true
     * @throws SQLException Veritabanı hatası durumunda
     */
    public boolean kmGuncelle(int aracId, int km) throws SQLException {
        String sql = "UPDATE Arac SET km = ? WHERE aracId = ? AND aktif = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, km);
            stmt.setInt(2, aracId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * ResultSet'ten Arac nesnesine dönüşüm yapar
     * @param rs ResultSet
     * @return Arac nesnesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    private Arac resultSetToArac(ResultSet rs) throws SQLException {
        Arac arac = new Arac();

        arac.setAracId(rs.getInt("aracId"));
        arac.setPlaka(rs.getString("plaka"));
        arac.setMarka(rs.getString("marka"));
        arac.setModel(rs.getString("model"));
        arac.setYil(rs.getInt("yil"));
        arac.setKm(rs.getInt("km"));
        arac.setKiralik(rs.getBoolean("kiralik"));
        arac.setFirma(rs.getString("firma"));

        String kiraBaslangicTarihiStr = rs.getString("kiraBaslangicTarihi");
        if (kiraBaslangicTarihiStr != null) {
            arac.setKiraBaslangicTarihi(LocalDate.parse(kiraBaslangicTarihiStr));
        }

        String kiraBitisTarihiStr = rs.getString("kiraBitisTarihi");
        if (kiraBitisTarihiStr != null) {
            arac.setKiraBitisTarihi(LocalDate.parse(kiraBitisTarihiStr));
        }

        arac.setAylikKiraBedeli(rs.getBigDecimal("aylikKiraBedeli"));
        arac.setAktif(rs.getBoolean("aktif"));

        Timestamp olusturmaTarihi = rs.getTimestamp("olusturmaTarihi");
        if (olusturmaTarihi != null) {
            arac.setOlusturmaTarihi(olusturmaTarihi.toLocalDateTime());
        }

        return arac;
    }
}