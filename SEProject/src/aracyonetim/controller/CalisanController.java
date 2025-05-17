package aracyonetim.controller;

import aracyonetim.dao.AracDAO;
import aracyonetim.dao.HarcamaDAO;
import aracyonetim.db.DBConnection;
import aracyonetim.model.Arac;
import aracyonetim.model.HarcamaKalemi;
import aracyonetim.model.Kullanici;
import aracyonetim.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CalisanController {
    @FXML private TableView<Arac> aracTableView;
    @FXML private TableColumn<Arac, String> plakaColumn;
    @FXML private TableColumn<Arac, String> markaColumn;
    @FXML private TableColumn<Arac, String> modelColumn;
    @FXML private TableColumn<Arac, Integer> yilColumn;
    @FXML private TableColumn<Arac, Integer> kmColumn;
    
    @FXML private TableView<HarcamaKalemi> harcamaTableView;
    @FXML private TableColumn<HarcamaKalemi, LocalDate> tarihColumn;
    @FXML private TableColumn<HarcamaKalemi, String> harcamaTipiColumn;
    @FXML private TableColumn<HarcamaKalemi, BigDecimal> tutarColumn;
    @FXML private TableColumn<HarcamaKalemi, String> aciklamaColumn;
    
    @FXML private TextField kmField;
    @FXML private ComboBox<String> harcamaTipiComboBox;
    @FXML private TextField tutarField;
    @FXML private TextArea aciklamaArea;
    @FXML private DatePicker tarihPicker;
    
    @FXML private Label kullaniciLabel;
    
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
            loadUserVehicles();
            setupHarcamaTipiComboBox();
            
            // Set today's date as default for date picker
            tarihPicker.setValue(LocalDate.now());
            
            // Add listener for vehicle selection to load expenses
            aracTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    loadVehicleExpenses(newSelection.getAracId());
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
        
        // Expense table
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        harcamaTipiColumn.setCellValueFactory(new PropertyValueFactory<>("harcamaTipi"));
        tutarColumn.setCellValueFactory(new PropertyValueFactory<>("tutar"));
        aciklamaColumn.setCellValueFactory(new PropertyValueFactory<>("aciklama"));
    }
    
    private void setupHarcamaTipiComboBox() {
        harcamaTipiComboBox.getItems().addAll(
            "Yakıt",
            "Bakım",
            "Tamir",
            "Sigorta",
            "Vergi",
            "Diğer"
        );
    }
    
    private void loadUserVehicles() {
        try {
            // Get vehicles assigned to current user
            List<Arac> aracList = aracDAO.kullanicininAraclari(currentUser.getKullaniciId());
            aracTableView.getItems().setAll(aracList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Araçlar yüklenemedi: " + e.getMessage());
        }
    }
    
    private void loadVehicleExpenses(int aracId) {
        try {
            List<HarcamaKalemi> harcamaList = harcamaDAO.aracHarcamalariGetir(aracId);
            harcamaTableView.getItems().setAll(harcamaList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Harcamalar yüklenemedi: " + e.getMessage());
        }
    }
    
    @FXML
    private void harcamaEkle() {
        Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
        if (seciliArac == null) {
            showAlert("Lütfen bir araç seçin.");
            return;
        }
        
        String harcamaTipi = harcamaTipiComboBox.getValue();
        if (harcamaTipi == null || harcamaTipi.isEmpty()) {
            showAlert("Lütfen harcama tipi seçin.");
            return;
        }
        
        LocalDate tarih = tarihPicker.getValue();
        if (tarih == null) {
            tarih = LocalDate.now(); // Default to today if not selected
        }
        
        String tutarText = tutarField.getText().trim();
        if (tutarText.isEmpty()) {
            showAlert("Lütfen tutar girin.");
            return;
        }
        
        BigDecimal tutar;
        try {
            // Replace comma with dot for proper decimal parsing
            tutarText = tutarText.replace(',', '.');
            tutar = new BigDecimal(tutarText);
        } catch (NumberFormatException e) {
            showAlert("Geçersiz tutar formatı. Lütfen sadece rakam ve nokta kullanın.");
            return;
        }
        
        String aciklama = aciklamaArea.getText();
        
        try {
            // If it's a "fuel" expense, update the vehicle's km
            if (harcamaTipi.equals("Yakıt")) {
                String kmText = kmField.getText().trim();
                if (!kmText.isEmpty()) {
                    try {
                        int km = Integer.parseInt(kmText);
                        aracDAO.kmGuncelle(seciliArac.getAracId(), km);
                        
                        // Update the local object too to reflect changes immediately
                        seciliArac.setKm(km);
                        
                        // Refresh the vehicle list to show updated km
                        loadUserVehicles();
                    } catch (NumberFormatException e) {
                        showAlert("Geçersiz kilometre formatı.");
                    }
                }
            }
            
            // Add expense
            HarcamaKalemi harcama = new HarcamaKalemi();
            harcama.setAracId(seciliArac.getAracId());
            harcama.setPlaka(seciliArac.getPlaka());
            harcama.setTarih(tarih);
            harcama.setHarcamaTipi(harcamaTipi);
            harcama.setTutar(tutar);
            harcama.setAciklama(aciklama);
            
            int harcamaId = harcamaDAO.harcamaEkle(harcama);
            
            if (harcamaId > 0) {
                // Refresh expenses table
                loadVehicleExpenses(seciliArac.getAracId());
                
                // Clear fields
                clearFields();
                
                showAlert("Harcama başarıyla kaydedildi.");
            } else {
                showAlert("Harcama kaydedilemedi. Lütfen tekrar deneyin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            
            // Check for specific timestamp error
            if (errorMsg.contains("timestamp") || errorMsg.contains("date")) {
                showAlert("Tarih formatı hatası: Lütfen geçerli bir tarih seçin.");
            } else {
                showAlert("Harcama kaydedilemedi: " + errorMsg);
            }
        }
    }
    
    private void clearFields() {
        kmField.clear();
        harcamaTipiComboBox.setValue(null);
        tutarField.clear();
        aciklamaArea.clear();
        tarihPicker.setValue(LocalDate.now());
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
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgilendirme");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 