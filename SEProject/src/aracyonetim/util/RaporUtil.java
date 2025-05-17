package aracyonetim.util;

import aracyonetim.model.HarcamaKalemi;
import aracyonetim.model.HarcamaRaporu;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Stream;

public class RaporUtil {
    
    public static void raporuPDFOlarakKaydet(HarcamaRaporu rapor, String dosyaAdi) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dosyaAdi));
        document.open();

        // Add title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Araç Harcama Raporu", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // Add report details
        document.add(new Paragraph("Rapor Tarihi: " + LocalDate.now()));
        document.add(new Paragraph("Başlangıç Tarihi: " + rapor.getBaslangicTarihi()));
        document.add(new Paragraph("Bitiş Tarihi: " + rapor.getBitisTarihi()));
        document.add(new Paragraph("\n"));

        // Create table
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        
        // Add headers
        Stream.of("Tarih", "Araç Plaka", "Harcama Tipi", "Tutar", "Açıklama")
            .forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(columnTitle));
                table.addCell(header);
            });

        // Add data rows
        for (HarcamaKalemi harcama : rapor.getHarcamaKalemleri()) {
            table.addCell(harcama.getTarih().toString());
            table.addCell(harcama.getPlaka());
            table.addCell(harcama.getHarcamaTipi());
            table.addCell(harcama.getTutar().toString() + " TL");
            table.addCell(harcama.getAciklama());
        }

        document.add(table);
        document.close();
    }

    public static void raporuExcelOlarakKaydet(HarcamaRaporu rapor, String dosyaAdi) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Harcama Raporu");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Tarih", "Araç Plaka", "Harcama Tipi", "Tutar", "Açıklama"};
        
        // Style for headers
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Create headers
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        for (HarcamaKalemi harcama : rapor.getHarcamaKalemleri()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(harcama.getTarih().toString());
            row.createCell(1).setCellValue(harcama.getPlaka());
            row.createCell(2).setCellValue(harcama.getHarcamaTipi());
            row.createCell(3).setCellValue(harcama.getTutar().doubleValue());
            row.createCell(4).setCellValue(harcama.getAciklama());
        }

        // Autosize columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(dosyaAdi)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    public static void raporuCSVOlarakKaydet(HarcamaRaporu rapor, String dosyaAdi) throws IOException {
        FileWriter out = new FileWriter(dosyaAdi);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader("Tarih", "Araç Plaka", "Harcama Tipi", "Tutar", "Açıklama"))) {
            
            // Add data rows
            for (HarcamaKalemi harcama : rapor.getHarcamaKalemleri()) {
                printer.printRecord(
                    harcama.getTarih(),
                    harcama.getPlaka(),
                    harcama.getHarcamaTipi(),
                    harcama.getTutar(),
                    harcama.getAciklama()
                );
            }
        }
    }
} 