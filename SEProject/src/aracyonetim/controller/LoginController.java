package aracyonetim.controller;

import aracyonetim.dao.KullaniciDAO;
import aracyonetim.db.DBConnection;
import aracyonetim.model.Kullanici;
import aracyonetim.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private KullaniciDAO kullaniciDAO;

    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            kullaniciDAO = new KullaniciDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Veritabanı bağlantı hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Lütfen email ve şifre giriniz.");
            return;
        }

        System.out.println("Attempting login with: " + email + " / " + password);
        
        try {
            Kullanici kullanici = kullaniciDAO.kullaniciGiris(email, password);
            
            if (kullanici != null) {
                System.out.println("Login successful for: " + kullanici.getAd() + " " + kullanici.getSoyad());
                // Set user in session
                SessionManager.getInstance().setCurrentUser(kullanici);
                
                // Open appropriate view based on user role
                openAppropriateView(event, kullanici);
            } else {
                System.out.println("Login failed. No matching user found.");
                showAlert("Geçersiz email veya şifre.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Giriş hatası: " + e.getMessage());
        }
    }
    
    @FXML
    private void openSignUpForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aracyonetim/view/SignUpView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Araç Yönetim Sistemi - Yeni Kullanıcı Kaydı");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Kayıt formu açılırken hata oluştu: " + e.getMessage());
        }
    }
    
    @FXML
    private void continueWithoutAccount(ActionEvent event) {
        try {
            // Set a guest user in session
            Kullanici guestUser = new Kullanici();
            guestUser.setAd("Misafir");
            guestUser.setSoyad("Kullanıcı");
            guestUser.setRol("DIS_FIRMA_YETKILISI");
            guestUser.setFirma("Misafir");
            
            // Set guest user in session
            SessionManager.getInstance().setCurrentUser(guestUser);
            
            // Load DisFirmaView for guest access
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aracyonetim/view/DisFirmaView.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Araç Yönetim Sistemi - Misafir Erişimi");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Misafir erişimi sağlanırken hata oluştu: " + e.getMessage());
        }
    }

    private void openAppropriateView(ActionEvent event, Kullanici kullanici) throws IOException {
        String fxmlPath;
        String windowTitle;

        // Determine which view to open based on user role
        switch (kullanici.getRol()) {
            case "FIRMA_YETKILISI":
                fxmlPath = "/aracyonetim/view/FirmaYetkilisiView.fxml";
                windowTitle = "Araç Yönetim Sistemi - Firma Yetkilisi";
                break;
            case "CALISAN":
                fxmlPath = "/aracyonetim/view/CalisanView.fxml";
                windowTitle = "Araç Yönetim Sistemi - Çalışan";
                break;
            case "DIS_FIRMA_YETKILISI":
                fxmlPath = "/aracyonetim/view/DisFirmaView.fxml";
                windowTitle = "Araç Yönetim Sistemi - Dış Firma Yetkilisi";
                break;
            default:
                showAlert("Bilinmeyen kullanıcı rolü.");
                return;
        }

        // Load the appropriate view
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgilendirme");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}