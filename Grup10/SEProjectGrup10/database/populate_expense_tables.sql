-- Populate expense tables (YakitKaydi, DegisenParca)
-- This script adds sample data for fuel records and replaced parts that link to the expense items

-- YakitKaydi (Fuel Records) - Corresponding to 'Yakıt' type expense items
INSERT INTO YakitKaydi (arac_id, tarih, litre, tutar) VALUES
-- Car 1 (Toyota Corolla) fuel records
(1, date('now', '-60 days'), 70.83, 850.00),  -- Shell
(1, date('now', '-45 days'), 75.00, 900.00),  -- BP
(1, date('now', '-30 days'), 79.17, 950.00),  -- Total
(1, date('now', '-15 days'), 83.33, 1000.00), -- Opet

-- Car 2 (Honda Civic) fuel records
(2, date('now', '-58 days'), 66.67, 800.00),  -- Petrol Ofisi
(2, date('now', '-43 days'), 70.83, 850.00),  -- Shell
(2, date('now', '-28 days'), 75.00, 900.00),  -- BP
(2, date('now', '-13 days'), 79.17, 950.00),  -- Total

-- Car 3 (Ford Focus) fuel records
(3, date('now', '-55 days'), 62.50, 750.00),  -- Opet
(3, date('now', '-40 days'), 66.67, 800.00),  -- Petrol Ofisi
(3, date('now', '-25 days'), 70.83, 850.00),  -- Shell
(3, date('now', '-10 days'), 75.00, 900.00),  -- BP

-- Historical fuel records for Car 1 (Toyota Corolla)
-- 2021
(1, '2021-01-15', 54.17, 650.00),
(1, '2021-02-15', 55.83, 670.00),
(1, '2021-03-15', 56.67, 680.00),
(1, '2021-04-15', 58.33, 700.00),
(1, '2021-05-15', 60.00, 720.00),
(1, '2021-06-15', 62.50, 750.00),
(1, '2021-07-15', 65.00, 780.00),
(1, '2021-08-15', 66.67, 800.00),
(1, '2021-09-15', 68.33, 820.00),
(1, '2021-10-15', 70.00, 840.00),
(1, '2021-11-15', 71.67, 860.00),
(1, '2021-12-15', 73.33, 880.00),

-- 2022
(1, '2022-01-15', 75.00, 900.00),
(1, '2022-02-15', 76.67, 920.00),
(1, '2022-03-15', 79.17, 950.00),
(1, '2022-04-15', 81.67, 980.00),
(1, '2022-05-15', 83.33, 1000.00),
(1, '2022-06-15', 87.50, 1050.00),
(1, '2022-07-15', 91.67, 1100.00),
(1, '2022-08-15', 95.83, 1150.00),
(1, '2022-09-15', 100.00, 1200.00),
(1, '2022-10-15', 104.17, 1250.00),
(1, '2022-11-15', 108.33, 1300.00),
(1, '2022-12-15', 112.50, 1350.00),

-- 2023
(1, '2023-01-15', 116.67, 1400.00),
(1, '2023-02-15', 120.83, 1450.00),
(1, '2023-03-15', 125.00, 1500.00),
(1, '2023-04-15', 129.17, 1550.00),
(1, '2023-05-15', 133.33, 1600.00),
(1, '2023-06-15', 137.50, 1650.00),
(1, '2023-07-15', 141.67, 1700.00),
(1, '2023-08-15', 145.83, 1750.00),
(1, '2023-09-15', 150.00, 1800.00),
(1, '2023-10-15', 154.17, 1850.00),
(1, '2023-11-15', 158.33, 1900.00),
(1, '2023-12-15', 162.50, 1950.00);

-- DegisenParca (Replaced Parts) - Corresponding to 'Tamir' and 'Bakım' type expense items
INSERT INTO DegisenParca (arac_id, parca_adi, tarih, fiyat) VALUES
-- Car 1 (Toyota Corolla) - Maintenance parts
(1, 'Motor Yağı', date('now', '-90 days'), 800.00),
(1, 'Yağ Filtresi', date('now', '-90 days'), 200.00),
(1, 'Hava Filtresi', date('now', '-90 days'), 150.00),
(1, 'Polen Filtresi', date('now', '-90 days'), 120.00),
(1, 'Yakıt Filtresi', date('now', '-90 days'), 230.00),
(1, 'Motor Yağı', date('now', '-20 days'), 900.00),
(1, 'Yağ Filtresi', date('now', '-20 days'), 220.00),
(1, 'Hava Filtresi', date('now', '-20 days'), 170.00),
(1, 'Polen Filtresi', date('now', '-20 days'), 130.00),
(1, 'Yakıt Filtresi', date('now', '-20 days'), 250.00),
(1, 'Triger Kayışı', date('now', '-20 days'), 580.00),
(1, 'Soğutma Suyu', date('now', '-20 days'), 120.00),

-- Car 1 (Toyota Corolla) - Repair parts
(1, 'Vites Kutusu', date('now', '-110 days'), 3200.00),
(1, 'Debriyaj Seti', date('now', '-110 days'), 1300.00),

-- Car 2 (Honda Civic) - Maintenance parts
(2, 'Motor Yağı', date('now', '-85 days'), 750.00),
(2, 'Yağ Filtresi', date('now', '-85 days'), 190.00),
(2, 'Hava Filtresi', date('now', '-85 days'), 140.00),
(2, 'Polen Filtresi', date('now', '-85 days'), 110.00),
(2, 'Yakıt Filtresi', date('now', '-85 days'), 210.00),
(2, 'Motor Yağı', date('now', '-15 days'), 850.00),
(2, 'Yağ Filtresi', date('now', '-15 days'), 210.00),
(2, 'Hava Filtresi', date('now', '-15 days'), 160.00),
(2, 'Polen Filtresi', date('now', '-15 days'), 120.00),
(2, 'Yakıt Filtresi', date('now', '-15 days'), 230.00),
(2, 'Triger Kayışı', date('now', '-15 days'), 530.00),
(2, 'Soğutma Suyu', date('now', '-15 days'), 100.00),

-- Car 2 (Honda Civic) - Repair parts
(2, 'Fren Disk Takımı', date('now', '-100 days'), 1500.00),
(2, 'Fren Balataları', date('now', '-100 days'), 800.00),
(2, 'ABS Sensörü', date('now', '-100 days'), 650.00),
(2, 'Fren Hidroliği', date('now', '-100 days'), 150.00),

-- Car 3 (Ford Focus) - Maintenance parts
(3, 'Motor Yağı', date('now', '-80 days'), 780.00),
(3, 'Yağ Filtresi', date('now', '-80 days'), 200.00),
(3, 'Hava Filtresi', date('now', '-80 days'), 150.00),
(3, 'Polen Filtresi', date('now', '-80 days'), 120.00),
(3, 'Yakıt Filtresi', date('now', '-80 days'), 220.00),
(3, 'Motor Yağı', date('now', '-10 days'), 880.00),
(3, 'Yağ Filtresi', date('now', '-10 days'), 220.00),
(3, 'Hava Filtresi', date('now', '-10 days'), 170.00),
(3, 'Polen Filtresi', date('now', '-10 days'), 130.00),
(3, 'Yakıt Filtresi', date('now', '-10 days'), 240.00),
(3, 'Triger Kayışı', date('now', '-10 days'), 560.00),
(3, 'Soğutma Suyu', date('now', '-10 days'), 110.00),

-- Car 3 (Ford Focus) - Repair parts
(3, 'Amortisör Takımı', date('now', '-95 days'), 1800.00),
(3, 'Süspansiyon Yayları', date('now', '-95 days'), 700.00),
(3, 'Rotil', date('now', '-95 days'), 400.00),

-- Historical parts records for Car 1 (corresponding to historical maintenance)
(1, 'Motor Yağı', '2021-03-20', 600.00),
(1, 'Yağ Filtresi', '2021-03-20', 150.00),
(1, 'Hava Filtresi', '2021-03-20', 120.00),
(1, 'Polen Filtresi', '2021-03-20', 100.00),
(1, 'Motor Yağı', '2021-09-20', 650.00),
(1, 'Yağ Filtresi', '2021-09-20', 160.00),
(1, 'Hava Filtresi', '2021-09-20', 130.00),
(1, 'Polen Filtresi', '2021-09-20', 110.00),
(1, 'Triger Kayışı', '2021-09-20', 500.00),

(1, 'Motor Yağı', '2022-03-20', 700.00),
(1, 'Yağ Filtresi', '2022-03-20', 170.00),
(1, 'Hava Filtresi', '2022-03-20', 140.00),
(1, 'Polen Filtresi', '2022-03-20', 120.00),
(1, 'Motor Yağı', '2022-09-20', 750.00),
(1, 'Yağ Filtresi', '2022-09-20', 180.00),
(1, 'Hava Filtresi', '2022-09-20', 150.00),
(1, 'Polen Filtresi', '2022-09-20', 130.00),
(1, 'Triger Kayışı', '2022-09-20', 540.00),

(1, 'Motor Yağı', '2023-03-20', 800.00),
(1, 'Yağ Filtresi', '2023-03-20', 190.00),
(1, 'Hava Filtresi', '2023-03-20', 160.00),
(1, 'Polen Filtresi', '2023-03-20', 140.00),
(1, 'Motor Yağı', '2023-09-20', 850.00),
(1, 'Yağ Filtresi', '2023-09-20', 200.00),
(1, 'Hava Filtresi', '2023-09-20', 170.00),
(1, 'Polen Filtresi', '2023-09-20', 150.00),
(1, 'Triger Kayışı', '2023-09-20', 580.00),

-- Historical repair parts for Car 1
(1, 'Fren Balataları', '2021-05-10', 800.00),
(1, 'Fren Hidroliği', '2021-05-10', 150.00),
(1, 'Far Ampulü', '2021-11-15', 300.00),
(1, 'Far Yuvası', '2021-11-15', 500.00),
(1, 'Şanzıman Yağı', '2022-02-22', 700.00),
(1, 'Şanzıman Dişlileri', '2022-02-22', 2800.00),
(1, 'Akü', '2022-08-05', 1500.00),
(1, 'Amortisör Takımı', '2023-01-18', 1900.00),
(1, 'Süspansiyon Yayları', '2023-01-18', 900.00),
(1, 'Motor Contası', '2023-07-25', 600.00),
(1, 'Piston Takımı', '2023-07-25', 2100.00),
(1, 'Krank Mili', '2023-07-25', 1800.00); 