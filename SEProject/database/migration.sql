-- Migration script to add firma column to Arac table

-- Add firma column if it doesn't exist
ALTER TABLE Arac ADD COLUMN firma TEXT;

-- Update existing records with default company
UPDATE Arac SET firma = 'Araç Filo A.Ş.' WHERE firma IS NULL;

-- Create a temporary table with the NOT NULL constraint
CREATE TABLE Arac_temp (
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
    firma TEXT NOT NULL,
    aktif BOOLEAN DEFAULT 1,
    olusturmaTarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    guncellemeTarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Copy data from old table to new table
INSERT INTO Arac_temp 
SELECT * FROM Arac;

-- Drop the old table
DROP TABLE Arac;

-- Rename the new table to the original name
ALTER TABLE Arac_temp RENAME TO Arac; 