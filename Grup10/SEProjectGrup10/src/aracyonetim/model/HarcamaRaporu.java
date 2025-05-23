package aracyonetim.model;

import aracyonetim.test.TestHarcamaRaporu;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Araç harcamalarına ilişkin rapor verilerini tutmak için kullanılan model sınıfı
 */
public class HarcamaRaporu {
    
    private String raporTipi; // bakım, kasko, yakıt, tamir, genel toplam
    private LocalDate baslangicTarihi;
    private LocalDate bitisTarihi;
    private Integer aracId; // null ise tüm araçlar
    private List<HarcamaKalemi> harcamaKalemleri;
    private BigDecimal toplamTutar;
    
    // Grafikler için kullanılacak özet veriler
    private Map<String, BigDecimal> kategoriTutarMapi;
    
    public HarcamaRaporu() {
        this.harcamaKalemleri = new ArrayList<>();
        this.kategoriTutarMapi = new HashMap<>();
        this.toplamTutar = BigDecimal.ZERO;
        TestHarcamaRaporu.testHarcamaRaporuOlustur();
    }
    
    /**
     * Rapora yeni bir harcama kalemi ekler ve toplam tutarı günceller
     * 
     * @param harcamaKalemi Eklenecek harcama kalemi 
     */
    public void harcamaEkle(HarcamaKalemi harcamaKalemi) {
        this.harcamaKalemleri.add(harcamaKalemi);
        this.toplamTutar = this.toplamTutar.add(harcamaKalemi.getTutar());
        
        // Kategori toplamını güncelle
        kategoriTutarMapi.putIfAbsent(harcamaKalemi.getHarcamaTipi(), BigDecimal.ZERO);
        kategoriTutarMapi.put(
            harcamaKalemi.getHarcamaTipi(), 
            kategoriTutarMapi.get(harcamaKalemi.getHarcamaTipi()).add(harcamaKalemi.getTutar())
        );
    }
    
    // Getters ve Setters
    
    public String getRaporTipi() {
        return raporTipi;
    }

    public void setRaporTipi(String raporTipi) {
        this.raporTipi = raporTipi;
    }

    public LocalDate getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public void setBaslangicTarihi(LocalDate baslangicTarihi) {
        this.baslangicTarihi = baslangicTarihi;
    }

    public LocalDate getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(LocalDate bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    public Integer getAracId() {
        return aracId;
    }

    public void setAracId(Integer aracId) {
        this.aracId = aracId;
    }

    public List<HarcamaKalemi> getHarcamaKalemleri() {
        return harcamaKalemleri;
    }

    public void setHarcamaKalemleri(List<HarcamaKalemi> harcamaKalemleri) {
        this.harcamaKalemleri = harcamaKalemleri;
        
        // Toplam tutarı sıfırla ve yeniden hesapla
        this.toplamTutar = BigDecimal.ZERO;
        this.kategoriTutarMapi.clear();
        
        // Tüm harcama kalemlerini döngüyle ele alarak toplam tutar ve kategori tutarlarını güncelle
        for (HarcamaKalemi kalem : harcamaKalemleri) {
            this.toplamTutar = this.toplamTutar.add(kalem.getTutar());
            
            // Kategori toplamını güncelle
            kategoriTutarMapi.putIfAbsent(kalem.getHarcamaTipi(), BigDecimal.ZERO);
            kategoriTutarMapi.put(
                kalem.getHarcamaTipi(), 
                kategoriTutarMapi.get(kalem.getHarcamaTipi()).add(kalem.getTutar())
            );
        }
    }

    public BigDecimal getToplamTutar() {
        return toplamTutar;
    }

    public Map<String, BigDecimal> getKategoriTutarMapi() {
        return kategoriTutarMapi;
    }
    
    @Override
    public String toString() {
        return "HarcamaRaporu{" +
                "raporTipi='" + raporTipi + '\'' +
                ", başlangıçTarihi=" + baslangicTarihi +
                ", bitişTarihi=" + bitisTarihi +
                ", toplamTutar=" + toplamTutar +
                '}';
    }
}
