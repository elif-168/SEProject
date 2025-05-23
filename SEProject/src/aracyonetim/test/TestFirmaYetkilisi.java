package aracyonetim.test;

import aracyonetim.model.FirmaYetkilisi;
import aracyonetim.model.HarcamaRaporu;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TestFirmaYetkilisi {
    private static boolean testExecuted = false;
    
    public static void testFirmaYetkilisiOlustur() {
        if (testExecuted) {
            return;
        }
        testExecuted = true;
        
        System.out.println("\n=== Firma Yetkilisi Testi Başlıyor ===");
        
        // Test 1: Boş constructor ile firma yetkilisi oluşturma
        FirmaYetkilisi yetkili1 = new FirmaYetkilisi();
        System.out.println("Test 1 - Boş constructor ile oluşturulan firma yetkilisi:");
        System.out.println(yetkili1);
        
        // Test 2: Parametreli constructor ile firma yetkilisi oluşturma
        FirmaYetkilisi yetkili2 = new FirmaYetkilisi(
            "Ahmet", 
            "Yılmaz", 
            "ahmet@firma.com", 
            "5551234567", 
            "Test Firma A.Ş.", 
            "sifre123"
        );
        System.out.println("\nTest 2 - Parametreli constructor ile oluşturulan firma yetkilisi:");
        System.out.println(yetkili2);
        
        // Test 3: Rapor oluşturma fonksiyonlarını test etme
        testRaporOlusturma(yetkili2);
        
        System.out.println("\n=== Firma Yetkilisi Testleri Tamamlandı ===\n");
    }
    
    public static void testRaporOlusturma(FirmaYetkilisi yetkili) {
        System.out.println("\nTest 3 - Rapor Oluşturma Testi Başlıyor");
        
        LocalDate baslangicTarihi = LocalDate.now().minusMonths(1);
        LocalDate bitisTarihi = LocalDate.now();
        
        // Test 3: Bakım raporu oluşturma
        HarcamaRaporu bakimRaporu = yetkili.bakimRaporuOlustur(baslangicTarihi, bitisTarihi, null);
        System.out.println("\nTest 3 - Bakım Raporu:");
        System.out.println("Rapor Tipi: " + bakimRaporu.getRaporTipi());
        System.out.println("Başlangıç Tarihi: " + bakimRaporu.getBaslangicTarihi());
        System.out.println("Bitiş Tarihi: " + bakimRaporu.getBitisTarihi());
        
        // Test 4: Yakıt raporu oluşturma
        HarcamaRaporu yakitRaporu = yetkili.yakitRaporuOlustur(baslangicTarihi, bitisTarihi, null);
        System.out.println("\nTest 4 - Yakıt Raporu:");
        System.out.println("Rapor Tipi: " + yakitRaporu.getRaporTipi());
        System.out.println("Başlangıç Tarihi: " + yakitRaporu.getBaslangicTarihi());
        System.out.println("Bitiş Tarihi: " + yakitRaporu.getBitisTarihi());
        
        // Test 5: Genel toplam raporu oluşturma
        HarcamaRaporu genelRapor = yetkili.genelToplamRaporuOlustur(baslangicTarihi, bitisTarihi, null);
        System.out.println("\nTest 5 - Genel Toplam Raporu:");
        System.out.println("Rapor Tipi: " + genelRapor.getRaporTipi());
        System.out.println("Başlangıç Tarihi: " + genelRapor.getBaslangicTarihi());
        System.out.println("Bitiş Tarihi: " + genelRapor.getBitisTarihi());
        
        // Test 6: Rapor dışa aktarma
        System.out.println("\nTest 6 - Rapor Dışa Aktarma:");
        String dosyaYolu = "raporlar/test_raporu.pdf";
        boolean pdfSonuc = yetkili.raporuPDFOlarakAktar(genelRapor, dosyaYolu);
        System.out.println("PDF Aktarım Sonucu: " + (pdfSonuc ? "Başarılı" : "Başarısız"));
        
        System.out.println("\n Rapor Oluşturma Testleri Tamamlandı");
    }
} 