package aracyonetim.dao;

import aracyonetim.model.HarcamaKalemi;
import aracyonetim.model.HarcamaRaporu;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Harcama işlemleri için DAO sınıfı
 */
public class HarcamaDAO {
    private Connection connection;
    
    public HarcamaDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Yeni bir harcama kaydı ekler
     * @param harcama Eklenecek harcama
     * @return Eklenen harcamanın ID'si
     * @throws SQLException Veritabanı hatası durumunda
     */
    public int harcamaEkle(HarcamaKalemi harcama) throws SQLException {
        String sql = "INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, harcama.getAracId());
            
            // SQLite doesn't have a specific date type, it stores dates as TEXT
            // Format the date as ISO string (YYYY-MM-DD) which SQLite can understand
            stmt.setString(2, harcama.getTarih().toString());
            
            stmt.setString(3, harcama.getHarcamaTipi());
            stmt.setBigDecimal(4, harcama.getTutar());
            stmt.setString(5, harcama.getAciklama());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Harcama ekleme başarısız oldu, etkilenen satır yok");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    harcama.setId(generatedKeys.getInt(1));
                    return harcama.getId();
                } else {
                    throw new SQLException("Harcama ekleme başarısız oldu, ID alınamadı");
                }
            }
        }
    }

    /**
     * Bir araca ait tüm harcama kayıtlarını getirir
     * @param aracId Araç ID'si
     * @return Harcama listesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    public List<HarcamaKalemi> aracHarcamalariGetir(int aracId) throws SQLException {
        String sql = "SELECT hk.*, a.plaka FROM HarcamaKalemi hk " +
                    "JOIN Arac a ON hk.aracId = a.aracId " +
                    "WHERE hk.aracId = ? ORDER BY hk.tarih DESC";

        List<HarcamaKalemi> harcamaList = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, aracId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    harcamaList.add(resultSetToHarcama(rs));
                }
            }
        }
        
        return harcamaList;
    }
    
    /**
     * Belirli bir tarih aralığında bir araca ait harcama kayıtlarını getirir
     * @param aracId Araç ID'si
     * @param baslangicTarihi Başlangıç tarihi
     * @param bitisTarihi Bitiş tarihi
     * @return Harcama listesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    public List<HarcamaKalemi> aracHarcamalariGetirByDateRange(int aracId, LocalDate baslangicTarihi, LocalDate bitisTarihi) throws SQLException {
        String sql = "SELECT hk.*, a.plaka FROM HarcamaKalemi hk " +
                    "JOIN Arac a ON hk.aracId = a.aracId " +
                    "WHERE hk.aracId = ? AND hk.tarih BETWEEN ? AND ? " +
                    "ORDER BY hk.tarih DESC";

        List<HarcamaKalemi> harcamaList = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, aracId);
            stmt.setString(2, baslangicTarihi.toString());
            stmt.setString(3, bitisTarihi.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    harcamaList.add(resultSetToHarcama(rs));
                }
            }
        }

        return harcamaList;
    }

    /**
     * Belirli bir tipdeki harcama kayıtlarını getirir
     * @param harcamaTipi Harcama tipi
     * @return Harcama listesi
     * @throws SQLException Veritabanı hatası durumunda
     */
    public List<HarcamaKalemi> tipBazliHarcamalarGetir(String harcamaTipi) throws SQLException {
        String sql = "SELECT hk.*, a.plaka FROM HarcamaKalemi hk " +
                    "JOIN Arac a ON hk.aracId = a.aracId " +
                    "WHERE hk.harcamaTipi = ? ORDER BY hk.tarih DESC";

        List<HarcamaKalemi> harcamaList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, harcamaTipi);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    harcamaList.add(resultSetToHarcama(rs));
                }
            }
        }

        return harcamaList;
    }

    /**
     * Harcama raporu oluşturur
     * @param raporTipi Rapor tipi (araç bazlı, tarih bazlı, harcama tipi bazlı, genel toplam)
     * @param baslangicTarihi Başlangıç tarihi
     * @param bitisTarihi Bitiş tarihi
     * @param aracId Araç ID'si (null ise tüm araçlar)
     * @return Harcama raporu
     * @throws SQLException Veritabanı hatası durumunda
     */
    public HarcamaRaporu raporOlustur(String raporTipi, LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT hk.*, a.plaka FROM HarcamaKalemi hk " +
            "JOIN Arac a ON hk.aracId = a.aracId " +
            "WHERE hk.tarih BETWEEN ? AND ? ");
        
        if (aracId != null) {
            sqlBuilder.append("AND hk.aracId = ? ");
        }
        
        sqlBuilder.append("ORDER BY ");
        
        switch (raporTipi.toLowerCase()) {
            case "araç bazlı rapor":
                sqlBuilder.append("hk.aracId, hk.tarih DESC");
                break;
            case "harcama tipi bazlı rapor":
                sqlBuilder.append("hk.harcamaTipi, hk.tarih DESC");
                break;
            case "tarih bazlı rapor":
                sqlBuilder.append("hk.tarih DESC");
                break;
            default:
                sqlBuilder.append("hk.tarih DESC");
                break;
        }

        List<HarcamaKalemi> harcamaList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sqlBuilder.toString())) {
            // Use strings for dates in SQLite
            stmt.setString(1, baslangicTarihi.toString());
            stmt.setString(2, bitisTarihi.toString());
            
            if (aracId != null) {
                stmt.setInt(3, aracId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    harcamaList.add(resultSetToHarcama(rs));
                }
            }
        }

        HarcamaRaporu rapor = new HarcamaRaporu();
        rapor.setRaporTipi(raporTipi);
        rapor.setBaslangicTarihi(baslangicTarihi);
        rapor.setBitisTarihi(bitisTarihi);
        rapor.setHarcamaKalemleri(harcamaList);
        
        return rapor;
    }
    
    /**
     * Tüm zamanları kapsayan harcama raporu oluşturur (herhangi bir tarih filtresi olmadan)
     * @param raporTipi Rapor tipi (araç bazlı, tarih bazlı, harcama tipi bazlı, genel toplam)
     * @param aracId Araç ID'si (null ise tüm araçlar)
     * @return Harcama raporu
     * @throws SQLException Veritabanı hatası durumunda
     */
    public HarcamaRaporu raporOlusturTumZamanlar(String raporTipi, Integer aracId) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT hk.*, a.plaka FROM HarcamaKalemi hk " +
            "JOIN Arac a ON hk.aracId = a.aracId ");

        if (aracId != null) {
            sqlBuilder.append("WHERE hk.aracId = ? ");
        }

        sqlBuilder.append("ORDER BY ");
        
        switch (raporTipi.toLowerCase()) {
            case "araç bazlı rapor":
                sqlBuilder.append("hk.aracId, hk.tarih ASC");
                break;
            case "harcama tipi bazlı rapor":
                sqlBuilder.append("hk.harcamaTipi, hk.tarih ASC");
                break;
            case "tarih bazlı rapor":
                sqlBuilder.append("hk.tarih ASC");
                break;
            default:
                sqlBuilder.append("hk.tarih ASC");
                break;
        }

        List<HarcamaKalemi> harcamaList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sqlBuilder.toString())) {
            if (aracId != null) {
                stmt.setInt(1, aracId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    harcamaList.add(resultSetToHarcama(rs));
                }
            }
        }

        // Get the min and max dates from the results
        LocalDate minDate = LocalDate.now();
        LocalDate maxDate = LocalDate.now();
        
        if (!harcamaList.isEmpty()) {
            minDate = harcamaList.stream()
                    .map(HarcamaKalemi::getTarih)
                    .min(LocalDate::compareTo)
                    .orElse(LocalDate.now());
            
            maxDate = harcamaList.stream()
                    .map(HarcamaKalemi::getTarih)
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.now());
        }

        HarcamaRaporu rapor = new HarcamaRaporu();
        rapor.setRaporTipi(raporTipi);
        rapor.setBaslangicTarihi(minDate);
        rapor.setBitisTarihi(maxDate);
        rapor.setHarcamaKalemleri(harcamaList);
        
        return rapor;
    }
    
    private HarcamaKalemi resultSetToHarcama(ResultSet rs) throws SQLException {
        HarcamaKalemi harcama = new HarcamaKalemi();
        
        harcama.setId(rs.getInt("id"));
        harcama.setAracId(rs.getInt("aracId"));
        harcama.setPlaka(rs.getString("plaka"));
        
        // Handle date conversion from string to LocalDate
        String dateStr = rs.getString("tarih");
        try {
            harcama.setTarih(LocalDate.parse(dateStr));
        } catch (Exception e) {
            // If parsing fails, default to today's date
            harcama.setTarih(LocalDate.now());
        }
        
        harcama.setHarcamaTipi(rs.getString("harcamaTipi"));
        harcama.setTutar(rs.getBigDecimal("tutar"));
        harcama.setAciklama(rs.getString("aciklama"));
        
        return harcama;
    }
}
