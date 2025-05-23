package aracyonetim.test;

import aracyonetim.model.Arac;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestArac {
    private static boolean testExecuted = false;
    
    public static void testAracOlustur() {
        if (testExecuted) {
            return;
        }
        testExecuted = true;
        
        System.out.println("\n=== Arac Testi Başlıyor ===");
        
        // Test 1: Boş constructor ile araç oluşturma
        Arac arac1 = new Arac();
        System.out.println("Test 1 - Boş constructor ile oluşturulan araç:");
        System.out.println(arac1);
        
        // Test 2: Parametreli constructor ile araç oluşturma
        Arac arac2 = new Arac("34ABC123", "Toyota", "Corolla", 2020, 50000, "Test Firma");
        System.out.println("\nTest 2 - Parametreli constructor ile oluşturulan araç:");
        System.out.println(arac2);
        
        // Test 3: Setter metodları ile araç bilgilerini güncelleme
        arac2.setKm(55000);
        arac2.setKiralik(true);
        arac2.setKiraBaslangicTarihi(LocalDate.now());
        arac2.setKiraBitisTarihi(LocalDate.now().plusMonths(12));
        arac2.setAylikKiraBedeli(new BigDecimal("5000"));
        
        System.out.println("\nTest 3 - Güncellenmiş araç bilgileri:");
        System.out.println(arac2);
        
        System.out.println("\n=== Arac Testi Tamamlandı ===\n");
    }
} 