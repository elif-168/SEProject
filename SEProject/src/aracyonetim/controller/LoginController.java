package aracyonetim.controller;

import aracyonetim.dao.KullaniciDAO;
import aracyonetim.db.DBConnection;
import aracyonetim.model.Kullanici;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class LoginController {

    public LoginController() {
    }

    @FXML
    private TextField kullaniciAdi;
    @FXML
    private PasswordField sifre;
    @FXML
    private Label warningLabel;


    @FXML
    private void loginPressed(ActionEvent event) {
        String girilenKullaniciAdi = kullaniciAdi.getText();
        String girilenSifre = sifre.getText();

        if (girilenKullaniciAdi == null || girilenKullaniciAdi.isEmpty()) {
            warningLabel.setText("Kullanıcı adı boş olamaz.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            KullaniciDAO kullaniciDAO = new KullaniciDAO(conn);
            Kullanici kullanici = kullaniciDAO.kullaniciGetirByName(girilenKullaniciAdi);

            if (kullanici == null) {
                warningLabel.setText("Kullanıcı bulunamadı.");
            } else if (!kullanici.getSifre().equals(girilenSifre)) {
                warningLabel.setText("Şifre yanlış.");
            } else {
                // Only go to next stage if login is successful
                goToNextStage(event);
                return; // Important: don't continue execution after successful login
            }

        } catch (SQLException e) {
            e.printStackTrace();
            warningLabel.setText("Veritabanı hatası: " + e.getMessage());
        }
    }


    @FXML
    private void continueWithoutAccount(ActionEvent event) {
        // TODO: Handle continue without account logic here
        System.out.println("Continue without account clicked");
        try {
            // You might want to implement this based on your requirements
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/aracyonetim/view/ManagerView.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Vehicle Fleet Management System");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            warningLabel.setText("Görünüm yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    private void goToNextStage(ActionEvent event){
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/aracyonetim/view/ManagerView.fxml")));

            // Get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.setTitle("Vehicle Fleet Management System");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            warningLabel.setText("Görünüm yüklenirken hata oluştu: " + e.getMessage());
        }
    }
}