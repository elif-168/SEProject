package aracyonetim.model;

import aracyonetim.test.TestFirmaYetkilisi;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FirmaYetkilisi sınıfı, Kullanici sınıfını genişleterek
 * firma yetkililerine özgü raporlama fonksiyonlarını sağlar.
 */
public class FirmaYetkilisi extends Kullanici {

    public FirmaYetkilisi() {
        super();
        TestFirmaYetkilisi.testFirmaYetkilisiOlustur();
    }

    public FirmaYetkilisi(String ad, String soyad, String email, String telefon, String firma, String sifre) {
        super(ad, soyad, email, telefon, firma, "Firma Yetkilisi", sifre);
        TestFirmaYetkilisi.testFirmaYetkilisiOlustur();
    }

    /**
     * Belirtilen tarihe ve araç veya araç grubuna göre bakım harcama raporu oluşturur
     *
     * @param baslangicTarihi Rapor başlangıç tarihi
     * @param bitisTarihi Rapor bitiş tarihi
     * @param aracId Belirli bir araç ID'si, tüm araçlar için null
     * @return Bakım harcamalarını içeren HarcamaRaporu nesnesi
     */
    public HarcamaRaporu bakimRaporuOlustur(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) {
        // Veritabanından bakım harcamalarını çekme işlemi burada yapılacak
        HarcamaRaporu rapor = new HarcamaRaporu();
        rapor.setRaporTipi("Bakım");
        rapor.setBaslangicTarihi(baslangicTarihi);
        rapor.setBitisTarihi(bitisTarihi);

        // Veritabanı işlemleri burada olacak

        return rapor;
    }

    /**
     * Belirtilen tarihe ve araç veya araç grubuna göre kasko harcama raporu oluşturur
     *
     * @param baslangicTarihi Rapor başlangıç tarihi
     * @param bitisTarihi Rapor bitiş tarihi
     * @param aracId Belirli bir araç ID'si, tüm araçlar için null
     * @return Kasko harcamalarını içeren HarcamaRaporu nesnesi
     */
    public HarcamaRaporu kaskoRaporuOlustur(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) {
        HarcamaRaporu rapor = new HarcamaRaporu();
        rapor.setRaporTipi("Kasko");
        rapor.setBaslangicTarihi(baslangicTarihi);
        rapor.setBitisTarihi(bitisTarihi);

        // Veritabanı işlemleri burada olacak

        return rapor;
    }

    /**
     * Belirtilen tarihe ve araç veya araç grubuna göre yakıt harcama raporu oluşturur
     *
     * @param baslangicTarihi Rapor başlangıç tarihi
     * @param bitisTarihi Rapor bitiş tarihi
     * @param aracId Belirli bir araç ID'si, tüm araçlar için null
     * @return Yakıt harcamalarını içeren HarcamaRaporu nesnesi
     */
    public HarcamaRaporu yakitRaporuOlustur(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) {
        HarcamaRaporu rapor = new HarcamaRaporu();
        rapor.setRaporTipi("Yakıt");
        rapor.setBaslangicTarihi(baslangicTarihi);
        rapor.setBitisTarihi(bitisTarihi);

        // Veritabanı işlemleri burada olacak

        return rapor;
    }

    /**
     * Belirtilen tarihe ve araç veya araç grubuna göre tamir harcama raporu oluşturur
     *
     * @param baslangicTarihi Rapor başlangıç tarihi
     * @param bitisTarihi Rapor bitiş tarihi
     * @param aracId Belirli bir araç ID'si, tüm araçlar için null
     * @return Tamir harcamalarını içeren HarcamaRaporu nesnesi
     */
    public HarcamaRaporu tamirRaporuOlustur(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) {
        HarcamaRaporu rapor = new HarcamaRaporu();
        rapor.setRaporTipi("Tamir");
        rapor.setBaslangicTarihi(baslangicTarihi);
        rapor.setBitisTarihi(bitisTarihi);

        // Veritabanı işlemleri burada olacak

        return rapor;
    }

    /**
     * Belirtilen tarihe ve araç veya araç grubuna göre genel toplam harcama raporu oluşturur
     *
     * @param baslangicTarihi Rapor başlangıç tarihi
     * @param bitisTarihi Rapor bitiş tarihi
     * @param aracId Belirli bir araç ID'si, tüm araçlar için null
     * @return Genel toplam harcamaları içeren HarcamaRaporu nesnesi
     */
    public HarcamaRaporu genelToplamRaporuOlustur(LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) {
        HarcamaRaporu rapor = new HarcamaRaporu();
        rapor.setRaporTipi("Genel Toplam");
        rapor.setBaslangicTarihi(baslangicTarihi);
        rapor.setBitisTarihi(bitisTarihi);

        // Tüm harcama tiplerini birleştiren rapor oluşturma işlemi burada yapılacak

        return rapor;
    }

    /**
     * Belirtilen parametrelere göre rapor tipini seçerek uygun raporu oluşturur
     *
     * @param raporTipi Rapor tipi (bakım, kasko, yakıt, tamir, genel toplam)
     * @param baslangicTarihi Rapor başlangıç tarihi
     * @param bitisTarihi Rapor bitiş tarihi
     * @param aracId Belirli bir araç ID'si, tüm araçlar için null
     * @return İlgili harcama tipine ait HarcamaRaporu nesnesi
     */
    public HarcamaRaporu raporOlustur(String raporTipi, LocalDate baslangicTarihi, LocalDate bitisTarihi, Integer aracId) {
        switch (raporTipi.toLowerCase()) {
            case "bakım":
                return bakimRaporuOlustur(baslangicTarihi, bitisTarihi, aracId);
            case "kasko":
                return kaskoRaporuOlustur(baslangicTarihi, bitisTarihi, aracId);
            case "yakıt":
                return yakitRaporuOlustur(baslangicTarihi, bitisTarihi, aracId);
            case "tamir":
                return tamirRaporuOlustur(baslangicTarihi, bitisTarihi, aracId);
            case "genel toplam":
                return genelToplamRaporuOlustur(baslangicTarihi, bitisTarihi, aracId);
            default:
                throw new IllegalArgumentException("Geçersiz rapor tipi: " + raporTipi);
        }
    }

    /**
     * Harcama raporunu PDF formatında dışa aktarır
     *
     * @param rapor Dışa aktarılacak HarcamaRaporu nesnesi
     * @param dosyaYolu Kaydedilecek dosya yolu
     * @return İşlemin başarılı olup olmadığı
     */
    public boolean raporuPDFOlarakAktar(HarcamaRaporu rapor, String dosyaYolu) {
        // PDF oluşturma ve kaydetme işlemleri burada yapılacak
        System.out.println("PDF raporu oluşturuluyor: " + dosyaYolu);

        // İlgili rapor bilgilerinden PDF oluşturma kodları
        // Örnek: iText veya Apache PDFBox kütüphanesi kullanılabilir

        return true;
    }

    /**
     * Harcama raporunu Excel formatında dışa aktarır
     *
     * @param rapor Dışa aktarılacak HarcamaRaporu nesnesi
     * @param dosyaYolu Kaydedilecek dosya yolu
     * @return İşlemin başarılı olup olmadığı
     */
    public boolean raporuExcelOlarakAktar(HarcamaRaporu rapor, String dosyaYolu) {
        // Excel oluşturma ve kaydetme işlemleri burada yapılacak
        System.out.println("Excel raporu oluşturuluyor: " + dosyaYolu);

        // İlgili rapor bilgilerinden Excel oluşturma kodları
        // Örnek: Apache POI kütüphanesi kullanılabilir

        return true;
    }

    /**
     * Harcama raporunu CSV formatında dışa aktarır
     *
     * @param rapor Dışa aktarılacak HarcamaRaporu nesnesi
     * @param dosyaYolu Kaydedilecek dosya yolu
     * @return İşlemin başarılı olup olmadığı
     */
    public boolean raporuCSVOlarakAktar(HarcamaRaporu rapor, String dosyaYolu) {
        // CSV oluşturma ve kaydetme işlemleri burada yapılacak
        System.out.println("CSV raporu oluşturuluyor: " + dosyaYolu);

        // İlgili rapor bilgilerinden CSV oluşturma kodları
        // Java'nın standart I/O sınıfları kullanılabilir

        return true;
    }
}