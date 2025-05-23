package aracyonetim.model;

import aracyonetim.test.TestArac;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Araç bilgilerini temsil eden model sınıfı
 */
public class Arac {
    private Integer aracId;
    private String plaka;
    private String marka;
    private String model;
    private int yil;
    private int km;
    private boolean kiralik;
    private LocalDate kiraBaslangicTarihi;
    private LocalDate kiraBitisTarihi;
    private BigDecimal aylikKiraBedeli;
    private String firma;
    private boolean aktif;
    private LocalDateTime olusturmaTarihi;
    private LocalDateTime guncellemeTarihi;

    // Default constructor
    public Arac() {
        this.aktif = true;
        this.olusturmaTarihi = LocalDateTime.now();
        TestArac.testAracOlustur();
    }

    // Parameterized constructor
    public Arac(String plaka, String marka, String model, int yil, int km, String firma) {
        this();
        this.plaka = plaka;
        this.marka = marka;
        this.model = model;
        this.yil = yil;
        this.km = km;
        this.firma = firma;
        this.kiralik = false;
    }

    // Getters and Setters
    public Integer getAracId() {
        return aracId;
    }

    public void setAracId(Integer aracId) {
        this.aracId = aracId;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYil() {
        return yil;
    }

    public void setYil(int yil) {
        this.yil = yil;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public boolean isKiralik() {
        return kiralik;
    }

    public void setKiralik(boolean kiralik) {
        this.kiralik = kiralik;
    }

    public LocalDate getKiraBaslangicTarihi() {
        return kiraBaslangicTarihi;
    }

    public void setKiraBaslangicTarihi(LocalDate kiraBaslangicTarihi) {
        this.kiraBaslangicTarihi = kiraBaslangicTarihi;
    }

    public LocalDate getKiraBitisTarihi() {
        return kiraBitisTarihi;
    }

    public void setKiraBitisTarihi(LocalDate kiraBitisTarihi) {
        this.kiraBitisTarihi = kiraBitisTarihi;
    }

    public BigDecimal getAylikKiraBedeli() {
        return aylikKiraBedeli;
    }

    public void setAylikKiraBedeli(BigDecimal aylikKiraBedeli) {
        this.aylikKiraBedeli = aylikKiraBedeli;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public LocalDateTime getGuncellemeTarihi() {
        return guncellemeTarihi;
    }

    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) {
        this.guncellemeTarihi = guncellemeTarihi;
    }

    @Override
    public String toString() {
        return "Arac{" +
                "aracId=" + aracId +
                ", plaka='" + plaka + '\'' +
                ", marka='" + marka + '\'' +
                ", model='" + model + '\'' +
                ", yil=" + yil +
                ", km=" + km +
                ", kiralik=" + kiralik +
                ", firma='" + firma + '\'' +
                ", aylikKiraBedeli=" + aylikKiraBedeli +
                ", aktif=" + aktif +
                '}';
    }
}