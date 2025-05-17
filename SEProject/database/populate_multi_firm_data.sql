-- Populate database with multiple firms and users for each firm

-- Add more firms' authorized users (FIRMA_YETKILISI)
INSERT INTO Kullanici (ad, soyad, email, telefon, firma, rol, sifre) VALUES
('Fatma', 'Öztürk', 'fatma@logitech.com', '5551112233', 'Logitech Lojistik A.Ş.', 'FIRMA_YETKILISI', '123456'),
('Kemal', 'Yıldız', 'kemal@starfleet.com', '5552223344', 'Starfleet Taşımacılık Ltd.', 'FIRMA_YETKILISI', '123456'),
('Serkan', 'Güneş', 'serkan@anadolutasima.com', '5553334455', 'Anadolu Taşımacılık A.Ş.', 'FIRMA_YETKILISI', '123456'),
('Berna', 'Akman', 'berna@hizlitasima.com', '5554445566', 'Hızlı Taşıma Ltd.', 'FIRMA_YETKILISI', '123456');

-- Add employees for each firm (CALISAN)
INSERT INTO Kullanici (ad, soyad, email, telefon, firma, rol, sifre) VALUES
-- Logitech employees
('Ali', 'Kara', 'ali@logitech.com', '5555556677', 'Logitech Lojistik A.Ş.', 'CALISAN', '123456'),
('Zeynep', 'Demir', 'zeynep@logitech.com', '5556667788', 'Logitech Lojistik A.Ş.', 'CALISAN', '123456'),

-- Starfleet employees
('Murat', 'Şahin', 'murat@starfleet.com', '5557778899', 'Starfleet Taşımacılık Ltd.', 'CALISAN', '123456'),
('Ebru', 'Koç', 'ebru@starfleet.com', '5558889900', 'Starfleet Taşımacılık Ltd.', 'CALISAN', '123456'),

-- Anadolu employees
('Hakan', 'Yılmaz', 'hakan@anadolutasima.com', '5559990011', 'Anadolu Taşımacılık A.Ş.', 'CALISAN', '123456'),
('Deniz', 'Aksoy', 'deniz@anadolutasima.com', '5550001122', 'Anadolu Taşımacılık A.Ş.', 'CALISAN', '123456'),

-- Hızlı Taşıma employees
('Canan', 'Özkan', 'canan@hizlitasima.com', '5551122334', 'Hızlı Taşıma Ltd.', 'CALISAN', '123456'),
('Burak', 'Aydın', 'burak@hizlitasima.com', '5552233445', 'Hızlı Taşıma Ltd.', 'CALISAN', '123456');

-- External company representatives (DISKAYNAKLAR_YETKILISI)
INSERT INTO Kullanici (ad, soyad, email, telefon, firma, rol, sifre) VALUES
('Melis', 'Tuncer', 'melis@serviscenter.com', '5553344556', 'Servis Center A.Ş.', 'DISKAYNAKLAR_YETKILISI', '123456'),
('Orhan', 'Erdem', 'orhan@autoteknik.com', '5554455667', 'Oto Teknik Ltd.', 'DISKAYNAKLAR_YETKILISI', '123456'),
('Sevgi', 'Çelik', 'sevgi@teknomoto.com', '5555566778', 'Tekno Moto A.Ş.', 'DISKAYNAKLAR_YETKILISI', '123456');

-- Add vehicles for each company
INSERT INTO Arac (plaka, marka, model, yil, km) VALUES
-- Logitech vehicles
('34LOG01', 'Mercedes', 'Sprinter', 2021, 45000),
('34LOG02', 'Ford', 'Transit', 2022, 30000),
('34LOG03', 'Volkswagen', 'Crafter', 2020, 60000),

-- Starfleet vehicles
('34STR01', 'Scania', 'R450', 2021, 80000),
('34STR02', 'Volvo', 'FH16', 2022, 65000),
('34STR03', 'MAN', 'TGX', 2020, 95000),

-- Anadolu vehicles
('34ANA01', 'Renault', 'Master', 2021, 50000),
('34ANA02', 'Fiat', 'Ducato', 2022, 35000),
('34ANA03', 'Iveco', 'Daily', 2020, 70000),

-- Hızlı Taşıma vehicles
('34HIZ01', 'Mercedes', 'Vito', 2021, 40000),
('34HIZ02', 'Ford', 'Courier', 2022, 25000),
('34HIZ03', 'Volkswagen', 'Caddy', 2020, 55000);

-- Assign vehicles to employees
INSERT INTO KullaniciArac (kullaniciId, aracId) VALUES
-- Logitech
(4, 19),  -- Fatma -> Mercedes Sprinter
(5, 20),  -- Ali -> Ford Transit
(6, 21),  -- Zeynep -> VW Crafter

-- Starfleet
(7, 22),  -- Kemal -> Scania R450
(8, 23),  -- Murat -> Volvo FH16
(9, 24),  -- Ebru -> MAN TGX

-- Anadolu
(10, 25), -- Serkan -> Renault Master
(11, 26), -- Hakan -> Fiat Ducato
(12, 27), -- Deniz -> Iveco Daily

-- Hızlı Taşıma
(13, 28), -- Berna -> Mercedes Vito
(14, 29), -- Canan -> Ford Courier
(15, 30); -- Burak -> VW Caddy

-- Add expense records for the new vehicles
INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES
-- Logitech vehicles fuel expenses
(19, date('now', '-30 days'), 'Yakıt', 1200.00, 'Shell benzin istasyonu'),
(19, date('now', '-15 days'), 'Yakıt', 1250.00, 'BP benzin istasyonu'),
(20, date('now', '-28 days'), 'Yakıt', 1100.00, 'Total benzin istasyonu'),
(20, date('now', '-14 days'), 'Yakıt', 1150.00, 'Opet benzin istasyonu'),
(21, date('now', '-25 days'), 'Yakıt', 1300.00, 'Petrol Ofisi benzin istasyonu'),
(21, date('now', '-12 days'), 'Yakıt', 1350.00, 'Shell benzin istasyonu'),

-- Starfleet vehicles fuel expenses (trucking company, higher fuel costs)
(22, date('now', '-30 days'), 'Yakıt', 3500.00, 'Shell benzin istasyonu'),
(22, date('now', '-15 days'), 'Yakıt', 3600.00, 'BP benzin istasyonu'),
(23, date('now', '-28 days'), 'Yakıt', 3400.00, 'Total benzin istasyonu'),
(23, date('now', '-14 days'), 'Yakıt', 3450.00, 'Opet benzin istasyonu'),
(24, date('now', '-25 days'), 'Yakıt', 3600.00, 'Petrol Ofisi benzin istasyonu'),
(24, date('now', '-12 days'), 'Yakıt', 3650.00, 'Shell benzin istasyonu'),

-- Maintenance expenses
(19, date('now', '-60 days'), 'Bakım', 3500.00, 'Periyodik bakım - 10.000km'),
(22, date('now', '-70 days'), 'Bakım', 5000.00, 'Periyodik bakım - 10.000km'),
(25, date('now', '-65 days'), 'Bakım', 3200.00, 'Periyodik bakım - 10.000km'),
(28, date('now', '-75 days'), 'Bakım', 2800.00, 'Periyodik bakım - 10.000km'),

-- Repair expenses
(19, date('now', '-90 days'), 'Tamir', 5500.00, 'Şanzıman tamiri'),
(22, date('now', '-95 days'), 'Tamir', 8500.00, 'Motor revizyon'),
(25, date('now', '-85 days'), 'Tamir', 4800.00, 'Fren sistemi tamiri'),
(28, date('now', '-92 days'), 'Tamir', 3200.00, 'Elektrik sistemi tamiri'),

-- Insurance expenses
(19, date('now', '-180 days'), 'Sigorta', 15000.00, 'Yıllık kasko'),
(22, date('now', '-190 days'), 'Sigorta', 22000.00, 'Yıllık kasko'),
(25, date('now', '-200 days'), 'Sigorta', 14000.00, 'Yıllık kasko'),
(28, date('now', '-210 days'), 'Sigorta', 12000.00, 'Yıllık kasko');

-- Add fuel records for the new vehicles
INSERT INTO YakitKaydi (arac_id, tarih, litre, tutar) VALUES
-- Logitech vehicles
(19, date('now', '-30 days'), 100.00, 1200.00),
(19, date('now', '-15 days'), 104.17, 1250.00),
(20, date('now', '-28 days'), 91.67, 1100.00),
(20, date('now', '-14 days'), 95.83, 1150.00),
(21, date('now', '-25 days'), 108.33, 1300.00),
(21, date('now', '-12 days'), 112.50, 1350.00),

-- Starfleet vehicles (big trucks, high consumption)
(22, date('now', '-30 days'), 291.67, 3500.00),
(22, date('now', '-15 days'), 300.00, 3600.00),
(23, date('now', '-28 days'), 283.33, 3400.00),
(23, date('now', '-14 days'), 287.50, 3450.00),
(24, date('now', '-25 days'), 300.00, 3600.00),
(24, date('now', '-12 days'), 304.17, 3650.00);

-- Add replaced parts for the new vehicles
INSERT INTO DegisenParca (arac_id, parca_adi, tarih, fiyat) VALUES
-- Logitech Mercedes Sprinter maintenance parts
(19, 'Motor Yağı', date('now', '-60 days'), 1000.00),
(19, 'Yağ Filtresi', date('now', '-60 days'), 300.00),
(19, 'Hava Filtresi', date('now', '-60 days'), 250.00),
(19, 'Polen Filtresi', date('now', '-60 days'), 200.00),
(19, 'Yakıt Filtresi', date('now', '-60 days'), 350.00),
(19, 'Triger Kayışı', date('now', '-60 days'), 800.00),

-- Logitech Mercedes Sprinter repair parts
(19, 'Şanzıman Yağı', date('now', '-90 days'), 800.00),
(19, 'Şanzıman Dişlileri', date('now', '-90 days'), 3200.00),
(19, 'Debriyaj Seti', date('now', '-90 days'), 1500.00),

-- Starfleet Scania R450 maintenance parts
(22, 'Motor Yağı', date('now', '-70 days'), 1500.00),
(22, 'Yağ Filtresi', date('now', '-70 days'), 450.00),
(22, 'Hava Filtresi', date('now', '-70 days'), 400.00),
(22, 'Yakıt Filtresi', date('now', '-70 days'), 550.00),
(22, 'AdBlue Filtresi', date('now', '-70 days'), 350.00),
(22, 'Triger Kayışı', date('now', '-70 days'), 1200.00),

-- Starfleet Scania R450 repair parts
(22, 'Piston Takımı', date('now', '-95 days'), 3500.00),
(22, 'Silindir Kafası', date('now', '-95 days'), 2800.00),
(22, 'Motor Contası', date('now', '-95 days'), 900.00),
(22, 'Krank Mili', date('now', '-95 days'), 1300.00); 