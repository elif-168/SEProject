# Araç Yönetim Sistemi

Bu proje, şirket araçlarının yönetimi için geliştirilmiş bir JavaFX uygulamasıdır.

## Özellikler

- Araç ekleme, silme ve güncelleme
- Araçları çalışanlara atama
- Araç harcamalarını takip etme (yakıt, bakım, tamir, kasko)
- Harcama raporları oluşturma (PDF, Excel, CSV formatlarında)

## Gereksinimler

- Java 17 veya üzeri
- Maven
- SQLite

## Kurulum

1. Projeyi klonlayın:

```bash
git clone https://github.com/your-username/arac-yonetim-sistemi.git
cd arac-yonetim-sistemi
```

2. Maven bağımlılıklarını yükleyin:

```bash
mvn clean install
```

3. Uygulamayı çalıştırın:

```bash
mvn javafx:run
```

## Veritabanı Yapısı

Uygulama SQLite veritabanı kullanmaktadır. Veritabanı şeması aşağıdaki tablolardan oluşur:

- `Arac`: Araç bilgileri
- `Kullanici`: Çalışan bilgileri
- `BakimVeHasar`: Bakım ve hasar kayıtları
- `DegisenParca`: Değişen parça kayıtları
- `YakitKaydi`: Yakıt alım kayıtları

## Katkıda Bulunma

1. Bu depoyu fork edin
2. Yeni bir branch oluşturun (`git checkout -b feature/yeniOzellik`)
3. Değişikliklerinizi commit edin (`git commit -am 'Yeni özellik: XYZ'`)
4. Branch'inizi push edin (`git push origin feature/yeniOzellik`)
5. Pull Request oluşturun

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.
