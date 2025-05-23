package aracyonetim.test;

import aracyonetim.model.HarcamaRaporu;
import aracyonetim.model.HarcamaKalemi;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TestHarcamaRaporu {
    private static boolean testExecuted = false;
    
    public static void testHarcamaRaporuOlustur() {
        if (testExecuted) {
            return;
        }
        testExecuted = true;
        
        System.out.println("\n=== Harcama Raporu Testi Başlıyor ===");
        
        // Test 1: Boş constructor ile rapor oluşturma
        HarcamaRaporu rapor1 = new HarcamaRaporu();
        System.out.println("Test 1 - Boş constructor ile oluşturulan rapor:");
        System.out.println(rapor1);
        
        // Test 2: Rapor bilgilerini ayarlama
        HarcamaRaporu rapor2 = new HarcamaRaporu();
        rapor2.setRaporTipi("Aylık Harcama Raporu");
        rapor2.setBaslangicTarihi(LocalDate.now().minusMonths(1));
        rapor2.setBitisTarihi(LocalDate.now());
        rapor2.setAracId(1);
        
        // Test 3: Harcama kalemleri ekleme
        HarcamaKalemi harcama1 = new HarcamaKalemi(1, "34ABC123", LocalDate.now(), "Yakıt", new BigDecimal("500.50"), "Benzin alımı");
        HarcamaKalemi harcama2 = new HarcamaKalemi(1, "34ABC123", LocalDate.now(), "Bakım", new BigDecimal("1000.00"), "Periyodik bakım");
        
        rapor2.harcamaEkle(harcama1);
        rapor2.harcamaEkle(harcama2);
        
        System.out.println("\nTest 2 & 3 - Harcama kalemleri eklenmiş rapor:");
        System.out.println("Rapor Tipi: " + rapor2.getRaporTipi());
        System.out.println("Başlangıç Tarihi: " + rapor2.getBaslangicTarihi());
        System.out.println("Bitiş Tarihi: " + rapor2.getBitisTarihi());
        System.out.println("Araç ID: " + rapor2.getAracId());
        System.out.println("Toplam Tutar: " + rapor2.getToplamTutar() + " TL");
        System.out.println("Kategori Tutarları: " + rapor2.getKategoriTutarMapi());
        
        System.out.println("\n=== Harcama Raporu Testi Tamamlandı ===\n");
    }
} 