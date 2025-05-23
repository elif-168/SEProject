package aracyonetim.controller;

import aracyonetim.dao.AracDAO;
import aracyonetim.dao.HarcamaDAO;
import aracyonetim.dao.KullaniciAracDAO;
import aracyonetim.db.DBConnection;
import aracyonetim.model.Arac;
import aracyonetim.model.HarcamaKalemi;
import aracyonetim.model.HarcamaRaporu;
import aracyonetim.model.Kullanici;
import aracyonetim.util.RaporUtil;
import aracyonetim.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DisFirmaController {
    @FXML private TableView<Arac> aracTableView;
    @FXML private TableColumn<Arac, String> plakaColumn;
    @FXML private TableColumn<Arac, String> markaColumn;
    @FXML private TableColumn<Arac, String> modelColumn;
    @FXML private TableColumn<Arac, Integer> yilColumn;
    @FXML private TableColumn<Arac, Integer> kmColumn;
    
    @FXML private TableView<Arac> tumAraclarTableView;
    @FXML private TableColumn<Arac, String> tumAraclarPlakaColumn;
    @FXML private TableColumn<Arac, String> tumAraclarMarkaColumn;
    @FXML private TableColumn<Arac, String> tumAraclarModelColumn;
    @FXML private TableColumn<Arac, Integer> tumAraclarYilColumn;
    @FXML private TableColumn<Arac, Integer> tumAraclarKmColumn;
    
    @FXML private TableView<HarcamaKalemi> harcamaTableView;
    @FXML private TableColumn<HarcamaKalemi, LocalDate> tarihColumn;
    @FXML private TableColumn<HarcamaKalemi, String> harcamaTipiColumn;
    @FXML private TableColumn<HarcamaKalemi, BigDecimal> tutarColumn;
    @FXML private TableColumn<HarcamaKalemi, String> aciklamaColumn;
    
    @FXML private DatePicker baslangicTarihiPicker;
    @FXML private DatePicker bitisTarihiPicker;
    @FXML private ComboBox<String> raporTipiComboBox;
    @FXML private CheckBox tarihFiltreCheckBox;
    
    @FXML private BarChart<String, Number> harcamaGrafigi;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    
    @FXML private Label kullaniciLabel;
    @FXML private Label toplamHarcamaLabel;
    
    // Fields for vehicle management
    @FXML private TextField plakaField;
    @FXML private TextField markaField;
    @FXML private TextField modelField;
    @FXML private TextField yilField;
    @FXML private TextField kmField;
    
    private AracDAO aracDAO;
    private HarcamaDAO harcamaDAO;
    private Kullanici currentUser;

    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            aracDAO = new AracDAO(conn);
            harcamaDAO = new HarcamaDAO(conn);
            
            // Get current user from session
            currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                kullaniciLabel.setText("Hoş geldiniz, " + currentUser.getAd() + " " + currentUser.getSoyad());
            }
            
            setupTableColumns();
            loadAllVehicles();
            setupRaporTipiComboBox();
            
            // Set default date range (last month)
            bitisTarihiPicker.setValue(LocalDate.now());
            baslangicTarihiPicker.setValue(LocalDate.now().minusMonths(1));
            
            // Add listener for vehicle selection to load expenses
            aracTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    loadVehicleExpensesForDateRange(newSelection.getAracId());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Veritabanı bağlantı hatası: " + e.getMessage());
        }
    }
    
    private void setupTableColumns() {
        // Vehicle table
        plakaColumn.setCellValueFactory(new PropertyValueFactory<>("plaka"));
        markaColumn.setCellValueFactory(new PropertyValueFactory<>("marka"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yilColumn.setCellValueFactory(new PropertyValueFactory<>("yil"));
        kmColumn.setCellValueFactory(new PropertyValueFactory<>("km"));
        
        // All vehicles table
        if (tumAraclarTableView != null) {
            tumAraclarPlakaColumn.setCellValueFactory(new PropertyValueFactory<>("plaka"));
            tumAraclarMarkaColumn.setCellValueFactory(new PropertyValueFactory<>("marka"));
            tumAraclarModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
            tumAraclarYilColumn.setCellValueFactory(new PropertyValueFactory<>("yil"));
            tumAraclarKmColumn.setCellValueFactory(new PropertyValueFactory<>("km"));
        }
        
        // Expense table
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        tarihColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });
        harcamaTipiColumn.setCellValueFactory(new PropertyValueFactory<>("harcamaTipi"));
        tutarColumn.setCellValueFactory(new PropertyValueFactory<>("tutar"));
        aciklamaColumn.setCellValueFactory(new PropertyValueFactory<>("aciklama"));
    }
    
    private void setupRaporTipiComboBox() {
        raporTipiComboBox.getItems().addAll(
            "Araç Bazlı Rapor",
            "Harcama Tipi Bazlı Rapor",
            "Tarih Bazlı Rapor"
        );
        raporTipiComboBox.setValue("Araç Bazlı Rapor");
    }
    
    private void loadAllVehicles() {
        try {
            if (currentUser == null) {
                showAlert("Kullanıcı bilgisi bulunamadı.");
                return;
            }

            List<Arac> aracList = aracDAO.firmaAraclariGetir(currentUser.getFirma());
            aracTableView.getItems().setAll(aracList);
            
            // Update the second vehicles table if it exists
            if (tumAraclarTableView != null) {
                tumAraclarTableView.getItems().setAll(aracList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Araçlar yüklenemedi: " + e.getMessage());
        }
    }
    
    private void loadVehicleExpensesForDateRange(int aracId) {
        try {
            LocalDate baslangicTarihi = baslangicTarihiPicker.getValue();
            LocalDate bitisTarihi = bitisTarihiPicker.getValue();
            
            if (baslangicTarihi == null || bitisTarihi == null) {
                showAlert("Lütfen tarih aralığı seçin.");
                return;
            }
            
            List<HarcamaKalemi> harcamaList = harcamaDAO.aracHarcamalariGetirByDateRange(
                aracId, baslangicTarihi, bitisTarihi);
            
            harcamaTableView.getItems().setAll(harcamaList);
            
            // Calculate total expenses
            BigDecimal toplamHarcama = harcamaList.stream()
                .map(HarcamaKalemi::getTutar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            toplamHarcamaLabel.setText("Toplam Harcama: " + toplamHarcama + " TL");
            
            // Update chart
            updateChart(harcamaList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Harcamalar yüklenemedi: " + e.getMessage());
        }
    }
    
    private void updateChart(List<HarcamaKalemi> harcamaList) {
        harcamaGrafigi.getData().clear();
        
        // Group by expense type
        Map<String, BigDecimal> harcamaTipiToplam = new HashMap<>();
        
        for (HarcamaKalemi harcama : harcamaList) {
            String tip = harcama.getHarcamaTipi();
            BigDecimal tutar = harcama.getTutar();
            
            harcamaTipiToplam.put(tip, 
                harcamaTipiToplam.getOrDefault(tip, BigDecimal.ZERO).add(tutar));
        }
        
        // Create chart series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Harcama Tutarı (TL)");
        
        // Sort the entries to ensure consistent order
        List<Map.Entry<String, BigDecimal>> sortedEntries = harcamaTipiToplam.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toList());
        
        for (Map.Entry<String, BigDecimal> entry : sortedEntries) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(data);
        }
        
        harcamaGrafigi.getData().add(series);
        
        // Ensure all labels are visible
        harcamaGrafigi.setBarGap(3.0);
        harcamaGrafigi.setCategoryGap(10.0);
        
        // Use Platform.runLater to ensure proper layout after data is added
        Platform.runLater(() -> {
            // Force layout update
            harcamaGrafigi.layout();
            
            // Ensure axis labels are properly positioned
            xAxis.setTickLabelRotation(45);
            xAxis.setTickLabelGap(10);
            xAxis.setTickLength(10);
            
            // Force axis layout
            xAxis.layout();
            yAxis.layout();
            
            // Additional layout updates
            harcamaGrafigi.requestLayout();
            harcamaGrafigi.getScene().getRoot().requestLayout();
        });
    }
    
    @FXML
    private void raporOlustur() {
        try {
            // Get selected vehicle if any
            Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
            Integer aracId = seciliArac != null ? seciliArac.getAracId() : null;
            
            // Get report type
            String raporTipi = raporTipiComboBox.getValue();
            if (raporTipi == null) {
                raporTipi = "Araç Bazlı Rapor";
            }
            
            HarcamaRaporu rapor;
            
            // Check if date filtering is enabled
            if (tarihFiltreCheckBox == null || tarihFiltreCheckBox.isSelected()) {
                // Use date filtering
                LocalDate baslangicTarihi = baslangicTarihiPicker.getValue();
                LocalDate bitisTarihi = bitisTarihiPicker.getValue();
                
                if (baslangicTarihi == null || bitisTarihi == null) {
                    bitisTarihi = LocalDate.now();
                    baslangicTarihi = bitisTarihi.minusMonths(1);
                }
                
                // Generate report with date filtering
                rapor = harcamaDAO.raporOlustur(raporTipi, baslangicTarihi, bitisTarihi, aracId);
            } else {
                // Generate report without date filtering (all time)
                rapor = harcamaDAO.raporOlusturTumZamanlar(raporTipi, aracId);
            }
            
            // Display report
            StringBuilder reportText = new StringBuilder("Harcama Raporu\n\n");
            reportText.append("Araç: ").append(seciliArac != null ? seciliArac.getPlaka() + " " + seciliArac.getMarka() + " " + seciliArac.getModel() : "Tüm Araçlar").append("\n");
            reportText.append("Toplam Harcama: ").append(calculateTotal(rapor.getHarcamaKalemleri())).append(" TL\n\n");
            
            reportText.append(String.format("%-12s %-10s %-15s %-10s %-30s\n", "Tarih", "Plaka", "Harcama Tipi", "Tutar", "Açıklama"));
            reportText.append("----------------------------------------------------------------------------------------\n");
            
            for (HarcamaKalemi harcama : rapor.getHarcamaKalemleri()) {
                reportText.append(String.format("%-12s %-10s %-15s %-10s %-30s\n", 
                    harcama.getTarih(), 
                    harcama.getPlaka(), 
                    harcama.getHarcamaTipi(), 
                    harcama.getTutar() + " TL", 
                    harcama.getAciklama()));
            }
            
            // Show report
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Harcama Raporu");
            alert.setHeaderText(null);
            
            // Make the alert resizable and set a larger size
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(800, 600);
            
            // Create a TextArea for the report content
            TextArea textArea = new TextArea(reportText.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setFont(Font.font("Monospaced", FontWeight.NORMAL, 12));
            
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Rapor oluşturulurken hata oluştu: " + e.getMessage());
        }
    }
    
    @FXML
    private void raporuPDFOlarakAktar() {
        exportReport("pdf");
    }
    
    @FXML
    private void raporuExcelOlarakAktar() {
        exportReport("excel");
    }
    
    @FXML
    private void raporuCSVOlarakAktar() {
        exportReport("csv");
    }
    
    private void exportReport(String format) {
        Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
        Integer aracId = seciliArac != null ? seciliArac.getAracId() : null;
        
        LocalDate baslangicTarihi = baslangicTarihiPicker.getValue();
        LocalDate bitisTarihi = bitisTarihiPicker.getValue();
        
        if (baslangicTarihi == null || bitisTarihi == null) {
            showAlert("Lütfen tarih aralığı seçin.");
            return;
        }
        
        try {
            String raporTipi = raporTipiComboBox.getValue();
            
            HarcamaRaporu rapor = harcamaDAO.raporOlustur(raporTipi, baslangicTarihi, bitisTarihi, aracId);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String tarihStr = LocalDate.now().format(formatter);
            
            String dosyaAdi = "rapor_" + tarihStr;
            String mesaj = "";
            
            switch (format) {
                case "pdf":
                    dosyaAdi += ".pdf";
                    RaporUtil.raporuPDFOlarakKaydet(rapor, dosyaAdi);
                    mesaj = "PDF raporu başarıyla oluşturuldu: " + dosyaAdi;
                    break;
                case "excel":
                    dosyaAdi += ".xlsx";
                    RaporUtil.raporuExcelOlarakKaydet(rapor, dosyaAdi);
                    mesaj = "Excel raporu başarıyla oluşturuldu: " + dosyaAdi;
                    showSuccessAlert("Excel Raporu İndirildi", "Excel raporu başarıyla oluşturuldu ve indirildi.\nDosya adı: " + dosyaAdi);
                    return;
                case "csv":
                    dosyaAdi += ".csv";
                    RaporUtil.raporuCSVOlarakKaydet(rapor, dosyaAdi);
                    mesaj = "CSV raporu başarıyla oluşturuldu: " + dosyaAdi;
                    showSuccessAlert("CSV Raporu İndirildi", "CSV raporu başarıyla oluşturuldu ve indirildi.\nDosya adı: " + dosyaAdi);
                    return;
            }
            
            showAlert(mesaj);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Rapor oluşturulurken hata oluştu: " + e.getMessage());
        }
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setPrefSize(400, 200);
        alert.showAndWait();
    }
    
    @FXML
    private void tarihFiltrele() {
        Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
        if (seciliArac != null) {
            loadVehicleExpensesForDateRange(seciliArac.getAracId());
        } else {
            showAlert("Lütfen bir araç seçin.");
        }
    }
    
    @FXML
    private void maliyetTahminiHesapla() {
        
        Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
        if (seciliArac == null) {
            showAlert("Lütfen bir araç seçin.");
            return;
        }
        
        LocalDate baslangicTarihi = baslangicTarihiPicker.getValue();
        LocalDate bitisTarihi = bitisTarihiPicker.getValue();
        
        if (baslangicTarihi == null || bitisTarihi == null) {
            showAlert("Lütfen tarih aralığı seçin.");
            return;
        }
        
        try {
            // Get expenses for selected vehicle and date range
            List<HarcamaKalemi> harcamalar = harcamaDAO.aracHarcamalariGetirByDateRange(
                seciliArac.getAracId(), baslangicTarihi, bitisTarihi);
            
            if (harcamalar.isEmpty()) {
                showAlert("Seçilen tarih aralığında harcama verisi bulunamadı.");
                return;
            }
            
            // Group expenses by type and calculate weighted averages
            Map<String, List<HarcamaKalemi>> tipBazliHarcamalar = harcamalar.stream()
                .collect(Collectors.groupingBy(HarcamaKalemi::getHarcamaTipi));
            
            // Show weighted averages
            StringBuilder mesaj = new StringBuilder();
            mesaj.append("Ağırlıklı Ortalama Maliyet Tahmini (").append(seciliArac.getPlaka()).append("):\n\n");
            
            for (Map.Entry<String, List<HarcamaKalemi>> entry : tipBazliHarcamalar.entrySet()) {
                String tip = entry.getKey();
                List<HarcamaKalemi> tipHarcamalari = entry.getValue();
                
                // Sort expenses by date
                tipHarcamalari.sort((a, b) -> a.getTarih().compareTo(b.getTarih()));
                
                // Calculate weighted average for this type
                int n = tipHarcamalari.size();
                BigDecimal toplamAgirlik = BigDecimal.ZERO;
                BigDecimal agirlikliToplam = BigDecimal.ZERO;
                
                for (int i = 0; i < n; i++) {
                    BigDecimal agirlik = BigDecimal.valueOf(i + 1);
                    toplamAgirlik = toplamAgirlik.add(agirlik);
                    agirlikliToplam = agirlikliToplam.add(
                        tipHarcamalari.get(i).getTutar().multiply(agirlik));
                }
                
                BigDecimal agirlikliOrtalama = agirlikliToplam.divide(toplamAgirlik, 2, BigDecimal.ROUND_HALF_UP);
                
                mesaj.append(tip).append(": ").append(agirlikliOrtalama.setScale(2, BigDecimal.ROUND_HALF_UP)).append(" TL\n");
            }
            
            showAlert(mesaj.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Maliyet tahmini hesaplanırken hata oluştu: " + e.getMessage());
        }
    }
    
    @FXML
    private void cikisYap() {
        try {
            // Clear session
            SessionManager.getInstance().clearSession();
            
            // Return to login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aracyonetim/view/LoginView.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) aracTableView.getScene().getWindow();
            stage.setTitle("Araç Yönetim Sistemi - Giriş");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Çıkış yapılırken hata oluştu: " + e.getMessage());
        }
    }
    
    @FXML
    private void aracEkle() {
        try {
            // Validate input fields
            if (plakaField.getText().trim().isEmpty() ||
                markaField.getText().trim().isEmpty() ||
                modelField.getText().trim().isEmpty() ||
                yilField.getText().trim().isEmpty() ||
                kmField.getText().trim().isEmpty()) {
                
                showAlert("Lütfen tüm alanları doldurun.");
                return;
            }
            
            // Validate number fields
            int yil, km;
            try {
                yil = Integer.parseInt(yilField.getText().trim());
                km = Integer.parseInt(kmField.getText().trim());
                
                if (yil < 1900 || yil > 2100) {
                    showAlert("Geçerli bir yıl giriniz (1900-2100).");
                    return;
                }
                
                if (km < 0) {
                    showAlert("Kilometre negatif olamaz.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Yıl ve kilometre sayısal olmalıdır.");
                return;
            }
            
            // Create new vehicle
            Arac arac = new Arac();
            arac.setPlaka(plakaField.getText().trim());
            arac.setMarka(markaField.getText().trim());
            arac.setModel(modelField.getText().trim());
            arac.setYil(yil);
            arac.setKm(km);
            arac.setKiralik(false);
            arac.setAktif(true);
            
            // Set company from current user
            if (currentUser != null) {
                arac.setFirma(currentUser.getFirma());
            } else {
                showAlert("Kullanıcı bilgisi bulunamadı.");
                return;
            }
            
            // Save to database
            int aracId = aracDAO.ekle(arac);
            
            // Associate vehicle with current user's company
            if (currentUser != null) {
                KullaniciAracDAO kullaniciAracDAO = new KullaniciAracDAO(DBConnection.getConnection());
                kullaniciAracDAO.kullaniciAracAta(currentUser.getKullaniciId(), aracId);
            }
            
            // Refresh vehicle list
            loadAllVehicles();
            
            // Clear form fields
            clearVehicleFields();
            
            showAlert("Araç başarıyla eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Araç eklenirken hata oluştu: " + e.getMessage());
        }
    }
    
    private void clearVehicleFields() {
        if (plakaField != null) plakaField.clear();
        if (markaField != null) markaField.clear();
        if (modelField != null) modelField.clear();
        if (yilField != null) yilField.clear();
        if (kmField != null) kmField.clear();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgilendirme");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Calculates the total amount of expenses from the given list
     * @param harcamaKalemleri List of expense items
     * @return The total sum as BigDecimal
     */
    private BigDecimal calculateTotal(List<HarcamaKalemi> harcamaKalemleri) {
        return harcamaKalemleri.stream()
            .map(HarcamaKalemi::getTutar)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 