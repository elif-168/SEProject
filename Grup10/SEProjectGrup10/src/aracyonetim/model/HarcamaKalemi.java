package aracyonetim.model;

import aracyonetim.test.TestHarcamaKalemi;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Harcama kalemi bilgilerini temsil eden model sınıfı
 */
public class HarcamaKalemi {
    private int id;
    private int aracId;
    private String plaka;
    private LocalDate tarih;
    private String harcamaTipi;
    private BigDecimal tutar;
    private String aciklama;

    public HarcamaKalemi() {
        TestHarcamaKalemi.testHarcamaKalemiOlustur();
    }

    public HarcamaKalemi(int aracId, String plaka, LocalDate tarih, String harcamaTipi, BigDecimal tutar, String aciklama) {
        this();
        this.aracId = aracId;
        this.plaka = plaka;
        this.tarih = tarih;
        this.harcamaTipi = harcamaTipi;
        this.tutar = tutar;
        this.aciklama = aciklama;
    }

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

    public LocalDate getTarih() {
        return tarih;
    }

    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }

    public String getHarcamaTipi() {
        return harcamaTipi;
    }

    public void setHarcamaTipi(String harcamaTipi) {
        this.harcamaTipi = harcamaTipi;
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
