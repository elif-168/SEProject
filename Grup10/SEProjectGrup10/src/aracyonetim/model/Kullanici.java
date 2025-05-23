package aracyonetim.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Kullanici {
    private Integer kullaniciId;
    private String ad;
    private String soyad;
    private String kullaniciAdi;
    private String sifre;
    private String email;
    private String telefon;
    private String firma;
    private String rol;
    private String departman;
    private String firmaBilgisi;
    private boolean aktif;
    private Timestamp olusturmaTarihi;
    private LocalDateTime guncellemeTarihi;

    public Kullanici() {
        this.aktif = true;
        this.olusturmaTarihi = Timestamp.valueOf(LocalDateTime.now());
    }

    public Kullanici(String ad, String soyad, String email, String telefon, String firma, String rol, String sifre) {
        this();
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefon = telefon;
        this.firma = firma;
        this.rol = rol;
        this.sifre = sifre;
    }

    // Getter ve Setter'lar
    public Integer getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Integer kullaniciId) { this.kullaniciId = kullaniciId; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public String getKullaniciAdi() { return kullaniciAdi; }
    public void setKullaniciAdi(String kullaniciAdi) { this.kullaniciAdi = kullaniciAdi; }

    public String getSifre() { return sifre; }
    public void setSifre(String sifre) { this.sifre = sifre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getFirma() { return firma; }
    public void setFirma(String firma) { this.firma = firma; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getDepartman() { return departman; }
    public void setDepartman(String departman) { this.departman = departman; }

    public String getFirmaBilgisi() { return firmaBilgisi; }
    public void setFirmaBilgisi(String firmaBilgisi) { this.firmaBilgisi = firmaBilgisi; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public Timestamp getOlusturmaTarihi() { return olusturmaTarihi; }
    public void setOlusturmaTarihi(Timestamp olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }

    public LocalDateTime getGuncellemeTarihi() { return guncellemeTarihi; }
    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) { this.guncellemeTarihi = guncellemeTarihi; }

    @Override
    public String toString() {
        return ad + " " + soyad + " (" + email + ")";
    }
}
