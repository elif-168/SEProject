package aracyonetim.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Harcama kalemlerini temsil eden model sınıfı
 */
public class HarcamaKalemi {
    
    private int id;
    private int aracId;
    private String plaka;
    private String harcamaTipi; // Bakım, Yakıt, Tamir, Kasko vb.
    private LocalDate tarih;
    private BigDecimal tutar;
    private String aciklama;
    
    public HarcamaKalemi() {
    }

    public HarcamaKalemi(int id, int aracId, String plaka, String harcamaTipi, 
                         LocalDate tarih, BigDecimal tutar, String aciklama) {
        this.id = id;
        this.aracId = aracId;
        this.plaka = plaka;
        this.harcamaTipi = harcamaTipi;
        this.tarih = tarih;
        this.tutar = tutar;
        this.aciklama = aciklama;
    }

    // Getters ve Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAracId() {
        return aracId;
    }

    public void setAracId(int aracId) {
        this.aracId = aracId;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public String getHarcamaTipi() {
        return harcamaTipi;
    }

    public void setHarcamaTipi(String harcamaTipi) {
        this.harcamaTipi = harcamaTipi;
    }

    public LocalDate getTarih() {
        return tarih;
    }

    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }

    public BigDecimal getTutar() {
        return tutar;
    }

    public void setTutar(BigDecimal tutar) {
        this.tutar = tutar;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
    
    @Override
    public String toString() {
        return "HarcamaKalemi{" +
                "id=" + id +
                ", aracId=" + aracId +
                ", plaka='" + plaka + '\'' +
                ", harcamaTipi='" + harcamaTipi + '\'' +
                ", tarih=" + tarih +
                ", tutar=" + tutar +
                ", aciklama='" + aciklama + '\'' +
                '}';
    }
}
