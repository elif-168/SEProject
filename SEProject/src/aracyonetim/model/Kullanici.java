package aracyonetim.model;

import java.sql.Timestamp;

public class Kullanici {
    private int kullaniciId;
    private String ad;
    private String soyad;
    private String kullaniciAdi;
    private String sifre;
    private String email;
    private String telefon;
    private String rol;
    private String departman;
    private String firmaBilgisi;
    private boolean aktif;
    private Timestamp olusturmaTarihi;

    // Getter ve Setter'lar
    public int getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(int kullaniciId) { this.kullaniciId = kullaniciId; }

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
}
