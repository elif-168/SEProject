package aracyonetim.controller;

import aracyonetim.dao.AracDAO;
import aracyonetim.dao.KullaniciDAO;
import aracyonetim.db.DBConnection;
import aracyonetim.model.Arac;
import aracyonetim.model.Kullanici;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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

    private AracDAO aracDAO;
    private KullaniciDAO kullaniciDAO;

    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            aracDAO = new AracDAO(conn);
            setupTableColumns();
            araclariYukle();
            calisanlariYukle();

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

    private void calisanlariYukle() {
        List<Kullanici> kullanicilar = null;
        try {
            kullaniciDAO = new KullaniciDAO(DBConnection.getConnection());
            kullanicilar = kullaniciDAO.firmadakiKullanicilariGetir("Araç Filo A.Ş.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        kullaniciListView.getItems().setAll(kullanicilar);
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
        System.out.println("***");
        // if araç has a çalışan
        //print "bu araç zaten kullanılıyor
        //else
        //update database
        Arac seciliArac = aracTableView.getSelectionModel().getSelectedItem();
        Kullanici seciliKullanici = kullaniciListView.getSelectionModel().getSelectedItem();

        if(seciliKullanici == null || seciliArac == null){
            aracBilgisi.setText("Lütfen bir araç ve çalışan seçiniz. ");
        }
        else{
            
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
    private void aracSil() {
        Arac secilenArac = aracTableView.getSelectionModel().getSelectedItem();
        if (secilenArac == null) {
            showAlert("Lütfen silinecek bir araç seçin.");
            return;
        }

        try {
            boolean basarili = aracDAO.pasifYap(secilenArac.getAracId());
            if (basarili) {
                araclariYukle();
                showAlert("Araç başarıyla silindi.");
            } else {
                showAlert("Araç silinemedi.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Silme işlemi sırasında hata oluştu.");
        }
    }

    private void clearFields() {
        plakaField.clear();
        markaField.clear();
        modelField.clear();
        yilField.clear();
        kmField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgilendirme");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void raporOlustur() {}

    @FXML
    private void raporuPDFOlarakAktar() {}

    @FXML
    private void raporuExcelOlarakAktar() {}

    @FXML
    private void raporuCSVOlarakAktar() {}


}
