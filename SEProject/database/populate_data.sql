-- Populate database with sample data for testing reports

-- More cars
INSERT INTO Arac (plaka, marka, model, yil, km) VALUES
('34AB123', 'Mercedes', 'E200', 2021, 25000),
('34CD456', 'BMW', '320i', 2022, 15000),
('34EF789', 'Audi', 'A4', 2020, 35000),
('34GH101', 'Volkswagen', 'Passat', 2019, 45000),
('34IJ202', 'Toyota', 'Corolla', 2022, 18000),
('34KL303', 'Renault', 'Megane', 2021, 22000),
('34MN404', 'Ford', 'Focus', 2020, 30000),
('34OP505', 'Hyundai', 'i20', 2022, 12000),
('34PR606', 'Fiat', 'Egea', 2021, 20000),
('34ST707', 'Skoda', 'Octavia', 2020, 28000),
('34UV808', 'Volvo', 'S60', 2019, 40000),
('34WX909', 'Peugeot', '3008', 2022, 14000),
('34YZ010', 'Kia', 'Sportage', 2021, 19000),
('34AA111', 'Nissan', 'Qashqai', 2020, 32000),
('34BB222', 'Honda', 'Civic', 2022, 10000);

-- Expense records for different cars (FUEL)
INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES
(1, date('now', '-60 days'), 'Yakıt', 850.00, 'Shell benzin istasyonu'),
(1, date('now', '-45 days'), 'Yakıt', 900.00, 'BP benzin istasyonu'),
(1, date('now', '-30 days'), 'Yakıt', 950.00, 'Total benzin istasyonu'),
(1, date('now', '-15 days'), 'Yakıt', 1000.00, 'Opet benzin istasyonu'),
(2, date('now', '-58 days'), 'Yakıt', 800.00, 'Petrol Ofisi benzin istasyonu'),
(2, date('now', '-43 days'), 'Yakıt', 850.00, 'Shell benzin istasyonu'),
(2, date('now', '-28 days'), 'Yakıt', 900.00, 'BP benzin istasyonu'),
(2, date('now', '-13 days'), 'Yakıt', 950.00, 'Total benzin istasyonu'),
(3, date('now', '-55 days'), 'Yakıt', 750.00, 'Opet benzin istasyonu'),
(3, date('now', '-40 days'), 'Yakıt', 800.00, 'Petrol Ofisi benzin istasyonu'),
(3, date('now', '-25 days'), 'Yakıt', 850.00, 'Shell benzin istasyonu'),
(3, date('now', '-10 days'), 'Yakıt', 900.00, 'BP benzin istasyonu');

-- Expense records for maintenance
INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES
(1, date('now', '-90 days'), 'Bakım', 2500.00, 'Periyodik bakım - 10.000km'),
(1, date('now', '-20 days'), 'Bakım', 3000.00, 'Periyodik bakım - 20.000km'),
(2, date('now', '-85 days'), 'Bakım', 2300.00, 'Periyodik bakım - 10.000km'),
(2, date('now', '-15 days'), 'Bakım', 2800.00, 'Periyodik bakım - 20.000km'),
(3, date('now', '-80 days'), 'Bakım', 2400.00, 'Periyodik bakım - 10.000km'),
(3, date('now', '-10 days'), 'Bakım', 2900.00, 'Periyodik bakım - 20.000km'),
(4, date('now', '-75 days'), 'Bakım', 2200.00, 'Periyodik bakım - 10.000km'),
(5, date('now', '-70 days'), 'Bakım', 2100.00, 'Periyodik bakım - 10.000km');

-- Expense records for repairs
INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES
(1, date('now', '-110 days'), 'Tamir', 4500.00, 'Vites kutusu tamiri'),
(2, date('now', '-100 days'), 'Tamir', 3800.00, 'Fren sistemi tamiri'),
(3, date('now', '-95 days'), 'Tamir', 2900.00, 'Süspansiyon tamiri'),
(4, date('now', '-88 days'), 'Tamir', 1500.00, 'Far değişimi'),
(5, date('now', '-82 days'), 'Tamir', 3200.00, 'Kaporta tamiri'),
(6, date('now', '-78 days'), 'Tamir', 2800.00, 'Egzoz sistemi tamiri');

-- Expense records for insurance
INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES
(1, date('now', '-365 days'), 'Sigorta', 12000.00, 'Yıllık kasko'),
(1, date('now', '-1 days'), 'Sigorta', 14000.00, 'Yıllık kasko yenileme'),
(2, date('now', '-340 days'), 'Sigorta', 11500.00, 'Yıllık kasko'),
(3, date('now', '-320 days'), 'Sigorta', 11000.00, 'Yıllık kasko'),
(4, date('now', '-300 days'), 'Sigorta', 10500.00, 'Yıllık kasko'),
(5, date('now', '-280 days'), 'Sigorta', 10000.00, 'Yıllık kasko');

-- Historical expense records for prediction model (for Car ID 1)
INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES
-- 2021
(1, '2021-01-15', 'Yakıt', 650.00, 'Ocak 2021 yakıt gideri'),
(1, '2021-02-15', 'Yakıt', 670.00, 'Şubat 2021 yakıt gideri'),
(1, '2021-03-15', 'Yakıt', 680.00, 'Mart 2021 yakıt gideri'),
(1, '2021-04-15', 'Yakıt', 700.00, 'Nisan 2021 yakıt gideri'),
(1, '2021-05-15', 'Yakıt', 720.00, 'Mayıs 2021 yakıt gideri'),
(1, '2021-06-15', 'Yakıt', 750.00, 'Haziran 2021 yakıt gideri'),
(1, '2021-07-15', 'Yakıt', 780.00, 'Temmuz 2021 yakıt gideri'),
(1, '2021-08-15', 'Yakıt', 800.00, 'Ağustos 2021 yakıt gideri'),
(1, '2021-09-15', 'Yakıt', 820.00, 'Eylül 2021 yakıt gideri'),
(1, '2021-10-15', 'Yakıt', 840.00, 'Ekim 2021 yakıt gideri'),
(1, '2021-11-15', 'Yakıt', 860.00, 'Kasım 2021 yakıt gideri'),
(1, '2021-12-15', 'Yakıt', 880.00, 'Aralık 2021 yakıt gideri'),

-- 2022
(1, '2022-01-15', 'Yakıt', 900.00, 'Ocak 2022 yakıt gideri'),
(1, '2022-02-15', 'Yakıt', 920.00, 'Şubat 2022 yakıt gideri'),
(1, '2022-03-15', 'Yakıt', 950.00, 'Mart 2022 yakıt gideri'),
(1, '2022-04-15', 'Yakıt', 980.00, 'Nisan 2022 yakıt gideri'),
(1, '2022-05-15', 'Yakıt', 1000.00, 'Mayıs 2022 yakıt gideri'),
(1, '2022-06-15', 'Yakıt', 1050.00, 'Haziran 2022 yakıt gideri'),
(1, '2022-07-15', 'Yakıt', 1100.00, 'Temmuz 2022 yakıt gideri'),
(1, '2022-08-15', 'Yakıt', 1150.00, 'Ağustos 2022 yakıt gideri'),
(1, '2022-09-15', 'Yakıt', 1200.00, 'Eylül 2022 yakıt gideri'),
(1, '2022-10-15', 'Yakıt', 1250.00, 'Ekim 2022 yakıt gideri'),
(1, '2022-11-15', 'Yakıt', 1300.00, 'Kasım 2022 yakıt gideri'),
(1, '2022-12-15', 'Yakıt', 1350.00, 'Aralık 2022 yakıt gideri'),

-- 2023
(1, '2023-01-15', 'Yakıt', 1400.00, 'Ocak 2023 yakıt gideri'),
(1, '2023-02-15', 'Yakıt', 1450.00, 'Şubat 2023 yakıt gideri'),
(1, '2023-03-15', 'Yakıt', 1500.00, 'Mart 2023 yakıt gideri'),
(1, '2023-04-15', 'Yakıt', 1550.00, 'Nisan 2023 yakıt gideri'),
(1, '2023-05-15', 'Yakıt', 1600.00, 'Mayıs 2023 yakıt gideri'),
(1, '2023-06-15', 'Yakıt', 1650.00, 'Haziran 2023 yakıt gideri'),
(1, '2023-07-15', 'Yakıt', 1700.00, 'Temmuz 2023 yakıt gideri'),
(1, '2023-08-15', 'Yakıt', 1750.00, 'Ağustos 2023 yakıt gideri'),
(1, '2023-09-15', 'Yakıt', 1800.00, 'Eylül 2023 yakıt gideri'),
(1, '2023-10-15', 'Yakıt', 1850.00, 'Ekim 2023 yakıt gideri'),
(1, '2023-11-15', 'Yakıt', 1900.00, 'Kasım 2023 yakıt gideri'),
(1, '2023-12-15', 'Yakıt', 1950.00, 'Aralık 2023 yakıt gideri'),

-- Historical maintenance records
(1, '2021-03-20', 'Bakım', 1800.00, '2021 İlk bakım'),
(1, '2021-09-20', 'Bakım', 1900.00, '2021 İkinci bakım'),
(1, '2022-03-20', 'Bakım', 2100.00, '2022 İlk bakım'),
(1, '2022-09-20', 'Bakım', 2300.00, '2022 İkinci bakım'),
(1, '2023-03-20', 'Bakım', 2600.00, '2023 İlk bakım'),
(1, '2023-09-20', 'Bakım', 2900.00, '2023 İkinci bakım');

-- Historical repair records
INSERT INTO HarcamaKalemi (aracId, tarih, harcamaTipi, tutar, aciklama) VALUES
(1, '2021-05-10', 'Tamir', 1200.00, 'Fren balata değişimi'),
(1, '2021-11-15', 'Tamir', 800.00, 'Far değişimi'),
(1, '2022-02-22', 'Tamir', 3500.00, 'Şanzıman tamiri'),
(1, '2022-08-05', 'Tamir', 1500.00, 'Akü değişimi'),
(1, '2023-01-18', 'Tamir', 2800.00, 'Süspansiyon tamiri'),
(1, '2023-07-25', 'Tamir', 4500.00, 'Motor tamiri'); 