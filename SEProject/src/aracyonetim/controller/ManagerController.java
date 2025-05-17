package aracyonetim.controller;

import aracyonetim.dao.AracDAO;
import aracyonetim.dao.HarcamaDAO;
import aracyonetim.dao.KullaniciDAO;
import aracyonetim.dao.KullaniciAracDAO;
import aracyonetim.db.DBConnection;
import aracyonetim.model.Arac;
import aracyonetim.model.HarcamaKalemi;
import aracyonetim.model.HarcamaRaporu;
import aracyonetim.model.Kullanici;
import aracyonetim.util.RaporUtil;
import aracyonetim.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ManagerController {
    @FXML private TableView<Arac> aracTableView;
    @FXML private TableColumn<Arac, String> plakaColumn;
    @FXML private TableColumn<Arac, String> markaColumn;
    @FXML private TableColumn<Arac, String> modelColumn;
    @FXML private TableColumn<Arac, Integer> yilColumn;
    @FXML private TableColumn<Arac, Integer> kmColumn;
    @FXML private TableColumn<Arac, Boolean> kiralikColumn;

    @FXML private ListView<Kullanici> kullaniciListView; // ya da ListView<Kullanici> eğer nesne göstereceksen

    @FXML private TextField plakaField;
    @FXML private TextField markaField;
    @FXML private TextField modelField;
    @FXML private TextField yilField;
    @FXML private TextField kmField;

    @FXML private Label aracBilgisi;
    
    // Rapor tabı bileşenleri
    @FXML private ComboBox<String> raporTipiComboBox;
    @FXML private DatePicker baslangicTarihi;
    @FXML private DatePicker bitisTarihi;
    @FXML private ComboBox<Arac> aracSecimComboBox;
    @FXML private RadioButton tabloRadioButton;
    @FXML private RadioButton grafikRadioButton;
    @FXML private RadioButton herIkisiRadioButton;
    @FXML private CheckBox tarihFiltreCheckBox;
    @FXML private TableView<HarcamaKalemi> raporTableView;
    @FXML private PieChart harcamaPieChart;
    @FXML private BarChart<String, Number> harcamaBarChart;
    @FXML private TabPane raporGoruntulemeTabs;
    
    @FXML private ComboBox<Arac> tahminAracComboBox;
    @FXML private ComboBox<String> tahminSuresiComboBox;
    @FXML private BarChart<String, Number> tahminBarChart;

    private AracDAO aracDAO;
    private KullaniciDAO kullaniciDAO;
    private KullaniciAracDAO kullaniciAracDAO;
    private HarcamaDAO harcamaDAO;

    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            aracDAO = new AracDAO(conn);
            kullaniciDAO = new KullaniciDAO(conn);
            kullaniciAracDAO = new KullaniciAracDAO(conn);
            harcamaDAO = new HarcamaDAO(conn);
            
            setupTableColumns();
            araclariYukle();
            calisanlariYukle();
            setupRaporComponents();
            setupMaliyetTahminiComponents();

            aracTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    aracBilgisi.setText(newValue.getMarka() + " "+ newValue.getModel() + " modeli olan "  + newValue.getPlaka() + " plakalı araç seçilmiştir");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Veritabanı bağlantı hatası.");
        }
    }

    private void setupRaporComponents() {
        // Rapor tipi combobox'ını doldur
        raporTipiComboBox.setItems(FXCollections.observableArrayList(
            "Bakım", "Kasko", "Yakıt", "Tamir", "Genel Toplam"
        ));
        raporTipiComboBox.setValue("Genel Toplam");
        
        // Varsayılan tarihler
        baslangicTarihi.setValue(LocalDate.now().minusMonths(1));
        bitisTarihi.setValue(LocalDate.now());
        
        try {
            // Araç listesini doldur
            List<Arac> araclar = aracDAO.tumAktifAraclariGetir();
            ObservableList<Arac> aracListesi = FXCollections.observableArrayList(araclar);
            
            // Tüm araçlar için özel bir seçenek ekle
            Arac tumAraclar = new Arac();
            tumAraclar.setPlaka("Tüm Araçlar");
            aracListesi.add(0, tumAraclar);
            
            aracSecimComboBox.setItems(aracListesi);
            aracSecimComboBox.getSelectionModel().selectFirst();
            
            // Araçları string olarak göster
            aracSecimComboBox.setConverter(new javafx.util.StringConverter<Arac>() {
                @Override
                public String toString(Arac arac) {
                    return arac.getPlaka();
                }
                
                @Override
                public Arac fromString(String string) {
                    return aracSecimComboBox.getItems().stream()
                            .filter(ap -> ap.getPlaka().equals(string))
                            .findFirst().orElse(null);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Araç listesi yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void raporOlustur() {
        try {
            // Get report type
            String raporTipi = raporTipiComboBox.getValue();
            if (raporTipi == null) {
                showAlert("Lütfen rapor tipi seçin.");
                return;
            }
            
            // Get selected vehicle if any
            Arac seciliArac = null;
            Integer aracId = null;
            
            if (aracSecimComboBox.getValue() != null) {
                seciliArac = aracSecimComboBox.getValue();
                if (!"Tüm Araçlar".equals(seciliArac.getPlaka())) {
                    aracId = seciliArac.getAracId();
                }
            }
            
            HarcamaRaporu rapor;
            
            // Check if date filtering is enabled
            if (tarihFiltreCheckBox == null || tarihFiltreCheckBox.isSelected()) {
                // Use date filtering
                // Get date range
                LocalDate basTarihi = baslangicTarihi.getValue();
                LocalDate bitTarihi = bitisTarihi.getValue();
                
                if (basTarihi == null || bitTarihi == null) {
                    // Use last month as default if no date range is specified
                    bitTarihi = LocalDate.now();
                    basTarihi = bitTarihi.minusMonths(1);
                }
                
                // Generate report with date filtering
                rapor = harcamaDAO.raporOlustur(raporTipi, basTarihi, bitTarihi, aracId);
            } else {
                // Generate report without date filtering (all time)
                rapor = harcamaDAO.raporOlusturTumZamanlar(raporTipi, aracId);
            }
            
            // Update table
            raporTableView.getItems().clear();
            raporTableView.getItems().addAll(rapor.getHarcamaKalemleri());
            
            // Show appropriate tab based on selected format
            if (tabloRadioButton.isSelected()) {
                raporGoruntulemeTabs.getSelectionModel().select(0);  // Tablo
            } else if (grafikRadioButton.isSelected()) {
                raporGoruntulemeTabs.getSelectionModel().select(1);  // Grafik
                updateCharts(rapor);
            } else {
                // Both selected, default to table
                raporGoruntulemeTabs.getSelectionModel().select(0);
                updateCharts(rapor);
            }
            
            showAlert("Rapor başarıyla oluşturuldu.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Rapor oluşturulurken hata oluştu: " + e.getMessage());
        }
    }
    
    private void updateCharts(HarcamaRaporu rapor) {
        // Clear existing data
        harcamaPieChart.getData().clear();
        harcamaBarChart.getData().clear();
        
        // Group expenses by type
        Map<String, BigDecimal> typeToAmount = new HashMap<>();
        
        for (HarcamaKalemi harcama : rapor.getHarcamaKalemleri()) {
            String type = harcama.getHarcamaTipi();
            BigDecimal amount = harcama.getTutar();
            
            typeToAmount.put(type, 
                typeToAmount.getOrDefault(type, BigDecimal.ZERO).add(amount));
        }
        
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, BigDecimal> entry : typeToAmount.entrySet()) {
            pieChartData.add(new PieChart.Data(
                entry.getKey() + " (" + entry.getValue() + " TL)", 
                entry.getValue().doubleValue()));
        }
        harcamaPieChart.setData(pieChartData);
        
        // Create bar chart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Harcama Tutarı (TL)");
        
        for (Map.Entry<String, BigDecimal> entry : typeToAmount.entrySet()) {
            series.getData().add(
                new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        
        harcamaBarChart.getData().add(series);
    }

    private void calisanlariYukle() {
        List<Kullanici> kullanicilar = null;
        try {
            // Get current user's firm
            Kullanici currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                // Get employees from the current user's firm
                kullanicilar = kullaniciDAO.firmadakiKullanicilariGetir(currentUser.getFirma());
            } else {
                showAlert("Kullanıcı bilgisi alınamadı. Lütfen tekrar giriş yapınız.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Çalışanlar yüklenirken hata oluştu: " + e.getMessage());
        }
        
        if (kullanicilar != null) {
        kullaniciListView.getItems().setAll(kullanicilar);
        }
    }

    private void setupTableColumns() {
        plakaColumn.setCellValueFactory(new PropertyValueFactory<>("plaka"));
        markaColumn.setCellValueFactory(new PropertyValueFactory<>("marka"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yilColumn.setCellValueFactory(new PropertyValueFactory<>("yil"));
        kmColumn.setCellValueFactory(new PropertyValueFactory<>("km"));
        kiralikColumn.setCellValueFactory(new PropertyValueFactory<>("kiralik"));
    }

    private void araclariYukle() {
        try {
            List<Arac> aracList = aracDAO.tumAktifAraclariGetir();
            aracTableView.getItems().setAll(aracList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Araçlar yüklenemedi.");
        }
    }
    
    @FXML
    private void kullaniciAta(){
        Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
        Kullanici seciliKullanici = kullaniciListView.getSelectionModel().getSelectedItem();

        if (seciliArac == null) {
            showAlert("Lütfen bir araç seçiniz.");
            return;
        }
        
        if (seciliKullanici == null) {
            showAlert("Lütfen bir kullanıcı seçiniz.");
            return;
        }
        
        try {
            // Kullanıcıya araç atama işlemini gerçekleştir
            boolean sonuc = kullaniciAracDAO.kullaniciAracAta(seciliKullanici.getKullaniciId(), seciliArac.getAracId());
            
            if (sonuc) {
                showAlert("Araç başarıyla kullanıcıya atandı: " + seciliKullanici.getAd() + " " + seciliKullanici.getSoyad() + 
                          " -> " + seciliArac.getMarka() + " " + seciliArac.getModel() + " (" + seciliArac.getPlaka() + ")");
            } else {
                showAlert("Araç atama işlemi başarısız oldu.");
        }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Araç atama sırasında hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void aracEkle() {
        try {
            String plaka = plakaField.getText();
            String marka = markaField.getText();
            String model = modelField.getText();
            int yil = Integer.parseInt(yilField.getText());
            int km = Integer.parseInt(kmField.getText());

            Arac arac = new Arac();
            arac.setPlaka(plaka);
            arac.setMarka(marka);
            arac.setModel(model);
            arac.setYil(yil);
            arac.setKm(km);
            arac.setKiralik(false);  // Varsayılan olarak kirada değil
            arac.setAktif(true);

            aracDAO.ekle(arac);
            araclariYukle();
            clearFields();
            showAlert("Araç başarıyla eklendi.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Araç eklenirken hata oluştu.");
        }
    }

    @FXML
    public void clearFields() {
        plakaField.clear();
        markaField.clear();
        modelField.clear();
        yilField.clear();
        kmField.clear();
    }

    @FXML
    private void aracSil() {
        Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
        if (seciliArac == null) {
            showAlert("Lütfen silmek için bir araç seçin.");
            return;
        }

        try {
            aracDAO.pasifYap(seciliArac.getAracId());
            araclariYukle();  // Tabloyu güncelle
            showAlert(seciliArac.getPlaka() + " plakalı araç silindi.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Araç silinirken hata oluştu.");
        }
    }

    @FXML
    private void raporuPDFOlarakAktar() {
        try {
            // Get selected vehicle if any
            Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
            Integer aracId = seciliArac != null ? seciliArac.getAracId() : null;

            // Create date range for report (last month by default)
            LocalDate bitisTarihi = LocalDate.now();
            LocalDate baslangicTarihi = bitisTarihi.minusMonths(1);

            // Generate report
            HarcamaDAO harcamaDAO = new HarcamaDAO(DBConnection.getConnection());
            HarcamaRaporu rapor = harcamaDAO.raporOlustur("genel toplam", baslangicTarihi, bitisTarihi, aracId);

            // Export to PDF
            RaporUtil.raporuPDFOlarakKaydet(rapor, "rapor.pdf");
            showAlert("PDF raporu başarıyla oluşturuldu: rapor.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("PDF raporu oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void raporuExcelOlarakAktar() {
        try {
            // Get selected vehicle if any
            Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
            Integer aracId = seciliArac != null ? seciliArac.getAracId() : null;

            // Create date range for report (last month by default)
            LocalDate bitisTarihi = LocalDate.now();
            LocalDate baslangicTarihi = bitisTarihi.minusMonths(1);

            // Generate report
            HarcamaDAO harcamaDAO = new HarcamaDAO(DBConnection.getConnection());
            HarcamaRaporu rapor = harcamaDAO.raporOlustur("genel toplam", baslangicTarihi, bitisTarihi, aracId);

            // Export to Excel
            RaporUtil.raporuExcelOlarakKaydet(rapor, "rapor.xlsx");
            showAlert("Excel raporu başarıyla oluşturuldu: rapor.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Excel raporu oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void raporuCSVOlarakAktar() {
        try {
            // Get selected vehicle if any
            Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
            Integer aracId = seciliArac != null ? seciliArac.getAracId() : null;

            // Create date range for report (last month by default)
            LocalDate bitisTarihi = LocalDate.now();
            LocalDate baslangicTarihi = bitisTarihi.minusMonths(1);

            // Generate report
            HarcamaDAO harcamaDAO = new HarcamaDAO(DBConnection.getConnection());
            HarcamaRaporu rapor = harcamaDAO.raporOlustur("genel toplam", baslangicTarihi, bitisTarihi, aracId);

            // Export to CSV
            RaporUtil.raporuCSVOlarakKaydet(rapor, "rapor.csv");
            showAlert("CSV raporu başarıyla oluşturuldu: rapor.csv");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("CSV raporu oluşturulurken hata oluştu: " + e.getMessage());
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgilendirme");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void maliyetTahminiHesapla() {
        try {
            // Get selected vehicle
            Arac seciliArac = tahminAracComboBox.getValue();
            if (seciliArac == null) {
                showAlert("Lütfen bir araç seçin.");
                return;
            }
            
            // Get selected forecast period
            String tahminSuresiStr = tahminSuresiComboBox.getValue();
            if (tahminSuresiStr == null) {
                tahminSuresiStr = "3 Ay"; // Default to 3 months
            }
            
            // Parse forecast period
            int tahminAyi = 3; // Default to 3 months
            if (tahminSuresiStr.contains("1 Ay")) tahminAyi = 1;
            else if (tahminSuresiStr.contains("3 Ay")) tahminAyi = 3;
            else if (tahminSuresiStr.contains("6 Ay")) tahminAyi = 6;
            else if (tahminSuresiStr.contains("12 Ay")) tahminAyi = 12;
            
            // Create date range for historical data (use last 6 months for better accuracy)
            LocalDate bitisTarihi = LocalDate.now();
            LocalDate baslangicTarihi = bitisTarihi.minusMonths(6);
            
            // Generate report with historical data
            List<HarcamaKalemi> harcamalar = harcamaDAO.aracHarcamalariGetirByDateRange(
                seciliArac.getAracId(), baslangicTarihi, bitisTarihi);
            
            if (harcamalar.isEmpty()) {
                showAlert("Bu araç için geçmiş harcama verisi bulunamadı. Tahmin yapılamıyor.");
                return;
            }
            
            // Group expenses by type
            Map<String, List<HarcamaKalemi>> tipBazliHarcamalar = new HashMap<>();
            
            for (HarcamaKalemi harcama : harcamalar) {
                String tip = harcama.getHarcamaTipi();
                if (!tipBazliHarcamalar.containsKey(tip)) {
                    tipBazliHarcamalar.put(tip, new ArrayList<>());
                }
                tipBazliHarcamalar.get(tip).add(harcama);
            }
            
            // Calculate weighted moving average for each expense type
            Map<String, BigDecimal> tipBazliAylikTahmin = new HashMap<>();
            
            for (Map.Entry<String, List<HarcamaKalemi>> entry : tipBazliHarcamalar.entrySet()) {
                String tip = entry.getKey();
                List<HarcamaKalemi> harcamaListesi = entry.getValue();
                
                // Sort by date to calculate weighted moving average
                harcamaListesi.sort((h1, h2) -> h1.getTarih().compareTo(h2.getTarih()));
                
                // Calculate weighted average giving more importance to recent expenses
                BigDecimal toplamAgirlik = BigDecimal.ZERO;
                BigDecimal agirlikliToplam = BigDecimal.ZERO;
                
                for (int i = 0; i < harcamaListesi.size(); i++) {
                    // Weight factor (more recent = higher weight)
                    BigDecimal agirlik = BigDecimal.valueOf(i + 1);
                    BigDecimal tutar = harcamaListesi.get(i).getTutar();
                    
                    agirlikliToplam = agirlikliToplam.add(tutar.multiply(agirlik));
                    toplamAgirlik = toplamAgirlik.add(agirlik);
                }
                
                // Calculate weighted average monthly cost
                BigDecimal ortalamaMaliyet;
                if (toplamAgirlik.compareTo(BigDecimal.ZERO) > 0) {
                    ortalamaMaliyet = agirlikliToplam.divide(toplamAgirlik, 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    ortalamaMaliyet = BigDecimal.ZERO;
                }
                
                // Group expenses by month to determine monthly average
                Map<String, BigDecimal> aylikHarcamalar = new HashMap<>();
                for (HarcamaKalemi harcama : harcamaListesi) {
                    String ay = harcama.getTarih().getYear() + "-" + harcama.getTarih().getMonthValue();
                    aylikHarcamalar.put(ay, 
                        aylikHarcamalar.getOrDefault(ay, BigDecimal.ZERO).add(harcama.getTutar()));
                }
                
                // Calculate average monthly cost
                BigDecimal toplamHarcama = BigDecimal.ZERO;
                for (BigDecimal aylikHarcama : aylikHarcamalar.values()) {
                    toplamHarcama = toplamHarcama.add(aylikHarcama);
                }
                
                int aylikVeriSayisi = aylikHarcamalar.size();
                BigDecimal aylikOrtalama;
                if (aylikVeriSayisi > 0) {
                    aylikOrtalama = toplamHarcama.divide(BigDecimal.valueOf(aylikVeriSayisi), 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    aylikOrtalama = ortalamaMaliyet; // Use weighted average as fallback
                }
                
                // Store monthly forecast
                tipBazliAylikTahmin.put(tip, aylikOrtalama);
            }
            
            // Calculate total forecast for the specified period
            Map<String, BigDecimal> tipBazliTahmin = new HashMap<>();
            BigDecimal toplamTahmin = BigDecimal.ZERO;
            
            for (Map.Entry<String, BigDecimal> entry : tipBazliAylikTahmin.entrySet()) {
                String tip = entry.getKey();
                BigDecimal aylikTahmin = entry.getValue();
                BigDecimal toplamTahminTip = aylikTahmin.multiply(BigDecimal.valueOf(tahminAyi));
                
                tipBazliTahmin.put(tip, toplamTahminTip);
                toplamTahmin = toplamTahmin.add(toplamTahminTip);
            }
            
            // Show forecast summary
            StringBuilder mesaj = new StringBuilder();
            mesaj.append("Gelecek ").append(tahminAyi).append(" Aylık Maliyet Tahmini ")
                .append("(").append(seciliArac.getPlaka()).append("):\n\n");
            
            for (String tip : tipBazliTahmin.keySet()) {
                BigDecimal maliyet = tipBazliTahmin.get(tip);
                mesaj.append(tip).append(": ").append(maliyet.setScale(2, BigDecimal.ROUND_HALF_UP)).append(" TL\n");
            }
            
            mesaj.append("\nToplam Tahmini Maliyet: ").append(toplamTahmin.setScale(2, BigDecimal.ROUND_HALF_UP)).append(" TL");
            
            showAlert(mesaj.toString());
            
            // Update chart
            updateCostEstimationChart(tipBazliTahmin);
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Maliyet tahmini hesaplanırken hata oluştu: " + e.getMessage());
        }
    }
    
    private void updateCostEstimationChart(Map<String, BigDecimal> tipBazliMaliyet) {
        // Clear existing data
        tahminBarChart.getData().clear();
        
        // Create chart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tahmini Maliyet (TL)");
        
        for (Map.Entry<String, BigDecimal> entry : tipBazliMaliyet.entrySet()) {
            series.getData().add(
                new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        
        tahminBarChart.getData().add(series);
    }

    private void setupMaliyetTahminiComponents() {
        try {
            // Set up the vehicle combobox for cost calculations
            List<Arac> araclar = aracDAO.tumAktifAraclariGetir();
            ObservableList<Arac> aracListesi = FXCollections.observableArrayList(araclar);
            
            tahminAracComboBox.setItems(aracListesi);
            
            // Set up vehicle display converter
            tahminAracComboBox.setConverter(new javafx.util.StringConverter<Arac>() {
                @Override
                public String toString(Arac arac) {
                    if (arac == null) return "";
                    return arac.getPlaka() + " - " + arac.getMarka() + " " + arac.getModel();
                }
                
                @Override
                public Arac fromString(String string) {
                    return tahminAracComboBox.getItems().stream()
                            .filter(ap -> (ap.getPlaka() + " - " + ap.getMarka() + " " + ap.getModel()).equals(string))
                            .findFirst().orElse(null);
                }
            });
            
            // Select the first vehicle by default if available
            if (!aracListesi.isEmpty()) {
                tahminAracComboBox.getSelectionModel().selectFirst();
            }
            
            // Set up forecast period combobox
            ObservableList<String> tahminSureleri = FXCollections.observableArrayList(
                "1 Ay", "3 Ay", "6 Ay", "12 Ay");
            tahminSuresiComboBox.setItems(tahminSureleri);
            tahminSuresiComboBox.setValue("3 Ay");
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Maliyet tahmini bileşenleri yüklenirken hata oluştu: " + e.getMessage());
        }
    }
}