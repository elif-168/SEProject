-- Araç tablosu
CREATE TABLE IF NOT EXISTS Arac (
    aracId INTEGER PRIMARY KEY AUTOINCREMENT,
    plaka TEXT NOT NULL UNIQUE,
    marka TEXT NOT NULL,
    model TEXT NOT NULL,
    yil INTEGER NOT NULL,
    km INTEGER NOT NULL,
    kiralik BOOLEAN DEFAULT 0,
    kiraBaslangicTarihi TEXT,
    kiraBitisTarihi TEXT,
    aylikKiraBedeli DECIMAL(10,2),
    aktif BOOLEAN DEFAULT 1,
    olusturmaTarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    guncellemeTarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Kullanıcı tablosu - expanded to match model
CREATE TABLE IF NOT EXISTS Kullanici (
    kullaniciId INTEGER PRIMARY KEY AUTOINCREMENT,
    ad TEXT NOT NULL,
    soyad TEXT NOT NULL,
    kullaniciAdi TEXT,
    email TEXT UNIQUE,
    telefon TEXT,
    firma TEXT NOT NULL,
    firmaBilgisi TEXT,
    departman TEXT,
    rol TEXT NOT NULL,
    sifre TEXT NOT NULL,
    aktif BOOLEAN DEFAULT 1,
    olusturmaTarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    guncellemeTarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bakım ve Hasar tablosu
CREATE TABLE IF NOT EXISTS BakimVeHasar (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    arac_id INTEGER NOT NULL,
    tarih TEXT NOT NULL,
    tutar DECIMAL(10,2) NOT NULL,
    aciklama TEXT,
    FOREIGN KEY (arac_id) REFERENCES Arac(aracId)
);

-- Değişen Parça tablosu
CREATE TABLE IF NOT EXISTS DegisenParca (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    arac_id INTEGER NOT NULL,
    parca_adi TEXT NOT NULL,
    tarih TEXT NOT NULL,
    fiyat DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (arac_id) REFERENCES Arac(aracId)
);

-- Yakıt Kaydı tablosu
CREATE TABLE IF NOT EXISTS YakitKaydi (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    arac_id INTEGER NOT NULL,
    tarih TEXT NOT NULL,
    litre DECIMAL(10,2) NOT NULL,
    tutar DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (arac_id) REFERENCES Arac(aracId)
);

-- Kullanıcı-Araç ilişki tablosu
CREATE TABLE IF NOT EXISTS KullaniciArac (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    kullaniciId INTEGER NOT NULL,
    aracId INTEGER NOT NULL,
    atamaTarihi TEXT DEFAULT CURRENT_TIMESTAMP,
    aktif BOOLEAN DEFAULT 1,
    FOREIGN KEY (kullaniciId) REFERENCES Kullanici(kullaniciId),
    FOREIGN KEY (aracId) REFERENCES Arac(aracId)
);

-- Harcama Kalemi tablosu
CREATE TABLE IF NOT EXISTS HarcamaKalemi (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    aracId INTEGER NOT NULL,
    tarih TEXT NOT NULL,
    harcamaTipi TEXT NOT NULL,
    tutar DECIMAL(10,2) NOT NULL,
    aciklama TEXT,
    FOREIGN KEY (aracId) REFERENCES Arac(aracId)
);

-- Örnek veriler
INSERT INTO Kullanici (ad, soyad, email, telefon, firma, rol, sifre) VALUES
('Ahmet', 'Yılmaz', 'ahmet.yilmaz@example.com', '5551234567', 'Araç Filo A.Ş.', 'FIRMA_YETKILISI', '123456'),
('Mehmet', 'Kaya', 'mehmet.kaya@example.com', '5559876543', 'Araç Filo A.Ş.', 'CALISAN', '123456'),
('Ayşe', 'Demir', 'ayse.demir@example.com', '5553456789', 'Araç Filo A.Ş.', 'CALISAN', '123456'),
('Admin', 'User', 'admin@example.com', '5551234567', 'Araç Filo A.Ş.', 'FIRMA_YETKILISI', 'admin'),
('External', 'Company', 'external@example.com', '5551234567', 'Servis Company', 'DIS_FIRMA_YETKILISI', '123456');

INSERT INTO Arac (plaka, marka, model, yil, km) VALUES
('34ABC123', 'Toyota', 'Corolla', 2020, 50000),
('34DEF456', 'Honda', 'Civic', 2021, 35000),
('34GHI789', 'Ford', 'Focus', 2019, 75000);

-- Örnek Harcama kaydı ekleme
INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES
(1, date('now', '-10 days'), 'Yakıt', 500.00, 'Benzin dolumu'),
(1, date('now', '-5 days'), 'Bakım', 1200.00, 'Yağ değişimi'),
(2, date('now', '-15 days'), 'Tamir', 3500.00, 'Fren balataları değişimi'),
(2, date('now', '-3 days'), 'Yakıt', 450.00, 'Benzin dolumu'),
(3, date('now', '-20 days'), 'Sigorta', 2500.00, 'Kasko yenileme');

-- Örnek Kullanıcı-Araç ataması
INSERT INTO KullaniciArac (kullaniciId, aracId) VALUES
(2, 1),  -- Mehmet Kaya -> Toyota Corolla
(3, 2);  -- Ayşe Demir -> Honda Civic 