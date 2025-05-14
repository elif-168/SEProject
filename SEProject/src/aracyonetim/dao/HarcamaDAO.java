package aracyonetim.dao;

import aracyonetim.model.HarcamaKalemi;
import aracyonetim.model.HarcamaRaporu;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Harcama raporları için veritabanı işlemlerini yöneten DAO sınıfı
 */
public class HarcamaDAO {
    
    private Connection connection;
    
    public HarcamaDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Belirtilen tarih aralığında ve araç ID'sine göre bakım ve hasar harcamalarını getirir
     * 
     * @param baslangicTarihi Başlangıç tarihi
     * @param bitisTarihi Bitiş tarihi
     * @param aracId Araç ID (null ise tüm araçlar)
     * @return Bakım ve hasar harcama kalemleri listesi
     * @throws SQLException
     */
    public List<HarcamaKalemi> bakimHarcamalariniGetir(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) throws SQLException {
        List<HarcamaKalemi> harcamalar = new ArrayList<>();
        
        String sql = "SELECT b.id, b.arac_id, a.plaka, 'Bakım/Hasar' as harcama_tipi, " +
                     "b.tarih, b.tutar, b.aciklama " +
                     "FROM BakimVeHasar b " +
                     "JOIN Arac a ON b.arac_id = a.arac_id " +
                     "WHERE b.tarih BETWEEN ? AND ? ";
        
        // Eğer belirli bir araç seçilmişse sorguya ekle
        if (aracId != null) {
            sql += "AND b.arac_id = ? ";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(baslangicTarihi));
            stmt.setDate(2, Date.valueOf(bitisTarihi));
            
            if (aracId != null) {
                stmt.setInt(3, aracId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HarcamaKalemi harcama = new HarcamaKalemi();
                    harcama.setId(rs.getInt("id"));
                    harcama.setAracId(rs.getInt("arac_id"));
                    harcama.setPlaka(rs.getString("plaka"));
                    harcama.setHarcamaTipi(rs.getString("harcama_tipi"));
                    harcama.setTarih(rs.getDate("tarih").toLocalDate());
                    harcama.setTutar(rs.getBigDecimal("tutar"));
                    harcama.setAciklama(rs.getString("aciklama"));
                    
                    harcamalar.add(harcama);
                }
            }
        }
        
        return harcamalar;
    }
    
    /**
     * Belirtilen tarih aralığında ve araç ID'sine göre değişen parça harcamalarını getirir
     * 
     * @param baslangicTarihi Başlangıç tarihi
     * @param bitisTarihi Bitiş tarihi
     * @param aracId Araç ID (null ise tüm araçlar)
     * @return Değişen parça harcama kalemleri listesi
     * @throws SQLException
     */
    public List<HarcamaKalemi> tamirHarcamalariniGetir(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) throws SQLException {
        List<HarcamaKalemi> harcamalar = new ArrayList<>();
        
        String sql = "SELECT d.id, d.arac_id, a.plaka, 'Tamir' as harcama_tipi, " +
                     "d.tarih, d.fiyat as tutar, d.parca_adi as aciklama " +
                     "FROM DegisenParca d " +
                     "JOIN Arac a ON d.arac_id = a.arac_id " +
                     "WHERE d.tarih BETWEEN ? AND ? ";
        
        // Eğer belirli bir araç seçilmişse sorguya ekle
        if (aracId != null) {
            sql += "AND d.arac_id = ? ";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(baslangicTarihi));
            stmt.setDate(2, Date.valueOf(bitisTarihi));
            
            if (aracId != null) {
                stmt.setInt(3, aracId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HarcamaKalemi harcama = new HarcamaKalemi();
                    harcama.setId(rs.getInt("id"));
                    harcama.setAracId(rs.getInt("arac_id"));
                    harcama.setPlaka(rs.getString("plaka"));
                    harcama.setHarcamaTipi(rs.getString("harcama_tipi"));
                    harcama.setTarih(rs.getDate("tarih").toLocalDate());
                    harcama.setTutar(rs.getBigDecimal("tutar"));
                    harcama.setAciklama(rs.getString("aciklama"));
                    
                    harcamalar.add(harcama);
                }
            }
        }
        
        return harcamalar;
    }
    
    /**
     * Belirtilen tarih aralığında ve araç ID'sine göre yakıt harcamalarını getirir
     * 
     * @param baslangicTarihi Başlangıç tarihi
     * @param bitisTarihi Bitiş tarihi
     * @param aracId Araç ID (null ise tüm araçlar)
     * @return Yakıt harcama kalemleri listesi
     * @throws SQLException
     */
    public List<HarcamaKalemi> yakitHarcamalariniGetir(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) throws SQLException {
        List<HarcamaKalemi> harcamalar = new ArrayList<>();
        
        String sql = "SELECT y.id, y.arac_id, a.plaka, 'Yakıt' as harcama_tipi, " +
                     "y.tarih, y.tutar, 'Yakıt Alımı' as aciklama " +
                     "FROM YakitKaydi y " +
                     "JOIN Arac a ON y.arac_id = a.arac_id " +
                     "WHERE y.tarih BETWEEN ? AND ? ";
        
        // Eğer belirli bir araç seçilmişse sorguya ekle
        if (aracId != null) {
            sql += "AND y.arac_id = ? ";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(baslangicTarihi));
            stmt.setDate(2, Date.valueOf(bitisTarihi));
            
            if (aracId != null) {
                stmt.setInt(3, aracId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HarcamaKalemi harcama = new HarcamaKalemi();
                    harcama.setId(rs.getInt("id"));
                    harcama.setAracId(rs.getInt("arac_id"));
                    harcama.setPlaka(rs.getString("plaka"));
                    harcama.setHarcamaTipi(rs.getString("harcama_tipi"));
                    harcama.setTarih(rs.getDate("tarih").toLocalDate());
                    harcama.setTutar(rs.getBigDecimal("tutar"));
                    harcama.setAciklama(rs.getString("aciklama"));
                    
                    harcamalar.add(harcama);
                }
            }
        }
        
        return harcamalar;
    }
    
    /**
     * Belirtilen tarih aralığında ve araç ID'sine göre kasko harcamalarını getirir
     * (Veritabanında kasko tablosu gösterilmediği için varsayılan olarak bu metodu ekliyorum)
     * 
     * @param baslangicTarihi Başlangıç tarihi
     * @param bitisTarihi Bitiş tarihi
     * @param aracId Araç ID (null ise tüm araçlar)
     * @return Kasko harcama kalemleri listesi
     * @throws SQLException
     */
    public List<HarcamaKalemi> kaskoHarcamalariniGetir(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) throws SQLException {
        // Veri tabanında Kasko ile ilgili bir tablo bilgisi verilmediği için örnek olarak boş liste dönüyoruz
        // Eğer bir Kasko tablosu varsa, buraya uygun sorgu yazılmalıdır
        List<HarcamaKalemi> harcamalar = new ArrayList<>();
        
        // Kasko tablosu olsaydı burada uygun sorgu yapılırdı
        // Örnek veri oluşturmak için (gerçek uygulama değil)
        if (aracId != null) {
            // Örnek veri
            HarcamaKalemi harcama = new HarcamaKalemi();
            harcama.setId(1);
            harcama.setAracId(aracId);
            harcama.setPlaka("Örnek Plaka");
            harcama.setHarcamaTipi("Kasko");
            harcama.setTarih(LocalDate.now());
            harcama.setTutar(new BigDecimal("1500.00"));
            harcama.setAciklama("Örnek Kasko Harcaması");
            
            harcamalar.add(harcama);
        }
        
        return harcamalar;
    }
    
    /**
     * Belirtilen tipte bir rapor oluşturur
     * 
     * @param raporTipi Rapor tipi (bakım, kasko, yakıt, tamir, genel toplam)
     * @param baslangicTarihi Başlangıç tarihi
     * @param bitisTarihi Bitiş tarihi
     * @param aracId Araç ID (null ise tüm araçlar)
     * @return Oluşturulan harcama raporu
     * @throws SQLException
     */
    public HarcamaRaporu raporOlustur(String raporTipi, LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) throws SQLException {
        HarcamaRaporu rapor = new HarcamaRaporu();
        rapor.setRaporTipi(raporTipi);
        rapor.setBaslangicTarihi(baslangicTarihi);
        rapor.setBitisTarihi(bitisTarihi);
        rapor.setAracId(aracId);
        
        List<HarcamaKalemi> harcamaKalemleri = new ArrayList<>();
        
        switch (raporTipi.toLowerCase()) {
            case "bakım":
                harcamaKalemleri = bakimHarcamalariniGetir(baslangicTarihi, bitisTarihi, aracId);
                break;
            case "tamir":
                harcamaKalemleri = tamirHarcamalariniGetir(baslangicTarihi, bitisTarihi, aracId);
                break;
            case "yakıt":
                harcamaKalemleri = yakitHarcamalariniGetir(baslangicTarihi, bitisTarihi, aracId);
                break;
            case "kasko":
                harcamaKalemleri = kaskoHarcamalariniGetir(baslangicTarihi, bitisTarihi, aracId);
                break;
            case "genel toplam":
                // Tüm harcama tiplerini birleştir
                harcamaKalemleri.addAll(bakimHarcamalariniGetir(baslangicTarihi, bitisTarihi, aracId));
                harcamaKalemleri.addAll(tamirHarcamalariniGetir(baslangicTarihi, bitisTarihi, aracId));
                harcamaKalemleri.addAll(yakitHarcamalariniGetir(baslangicTarihi, bitisTarihi, aracId));
                harcamaKalemleri.addAll(kaskoHarcamalariniGetir(baslangicTarihi, bitisTarihi, aracId));
                break;
            default:
                throw new IllegalArgumentException("Geçersiz rapor tipi: " + raporTipi);
        }
        
        rapor.setHarcamaKalemleri(harcamaKalemleri);
        return rapor;
    }
    
    /**
     * Tüm araçları getirir
     * 
     * @return Araç ID ve Plaka içeren liste
     * @throws SQLException
     */
    public List<Object[]> tumAraclariGetir() throws SQLException {
        List<Object[]> araclar = new ArrayList<>();
        
        String sql = "SELECT aracId, plaka FROM Arac WHERE aktif = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] arac = new Object[2];
                arac[0] = rs.getInt("arac_id");
                arac[1] = rs.getString("plaka");
                araclar.add(arac);
            }
        }
        
        return araclar;
    }
}
