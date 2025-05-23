package aracyonetim.test;

import aracyonetim.model.HarcamaKalemi;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TestHarcamaKalemi {
    private static boolean testExecuted = false;
    
    public static void testHarcamaKalemiOlustur() {
        if (testExecuted) {
            return;
        }
        testExecuted = true;
        
        System.out.println("\n=== Harcama Kalemi Testi Başlıyor ===");
        
        // Test 1: Boş constructor ile harcama kalemi oluşturma
        HarcamaKalemi harcama1 = new HarcamaKalemi();
        System.out.println("Test 1 - Boş constructor ile oluşturulan harcama kalemi:");
        System.out.println(harcama1);
        
        // Test 2: Parametreli constructor ile harcama kalemi oluşturma
        HarcamaKalemi harcama2 = new HarcamaKalemi(
            1, 
            "34ABC123", 
            LocalDate.now(), 
            "Yakıt", 
            new BigDecimal("500.50"), 
            "Benzin alımı"
        );
        System.out.println("\nTest 2 - Parametreli constructor ile oluşturulan harcama kalemi:");
        System.out.println(harcama2);
        
        // Test 3: Setter metodları ile harcama bilgilerini güncelleme
        harcama2.setTutar(new BigDecimal("550.75"));
        harcama2.setAciklama("Benzin ve yağ alımı");
        
        System.out.println("\nTest 3 - Güncellenmiş harcama kalemi bilgileri:");
        System.out.println(harcama2);
        
        System.out.println("\n=== Harcama Kalemi Testi Tamamlandı ===\n");
    }
} 