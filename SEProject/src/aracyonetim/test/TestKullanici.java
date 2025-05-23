package aracyonetim.test;

import aracyonetim.model.Kullanici;

public class TestKullanici {
    public static void testKullaniciOlustur() {
        System.out.println("\n=== Kullanıcı Test Başlıyor ===");
        Kullanici kullanici = new Kullanici();
        kullanici.setKullaniciAdi("test_user");
        kullanici.setSifre("test123");
        kullanici.setAd("Test");
        kullanici.setSoyad("Kullanıcı");
        kullanici.setEmail("test@example.com");
        
        System.out.println("Test Kullanıcısı Oluşturuldu:");
        System.out.println("Kullanıcı Adı: " + kullanici.getKullaniciAdi());
        System.out.println("Ad: " + kullanici.getAd());
        System.out.println("Soyad: " + kullanici.getSoyad());
        System.out.println("Email: " + kullanici.getEmail());
        System.out.println("=== Kullanıcı Test Tamamlandı ===\n");
    }
} 