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
        // TODO: Handle login logic here
        System.out.println("Login button pressed");
        Kullanici kullanici = new Kullanici();
        try {
            KullaniciDAO kullaniciDAO = new KullaniciDAO(DBConnection.getConnection());
            kullanici = kullaniciDAO.kullaniciGetirByName(kullaniciAdi.getText());
            if (kullanici.getSifre().equals(sifre.getText())) {
                goToNextStage();
            }
            else {
                warningLabel.setText("giremedin zaa");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @FXML
    private void continueWithoutAccount(ActionEvent event) {
        // TODO: Handle continue without account logic here
        System.out.println("Continue without account clicked");
    }

    private void goToNextStage(){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("aracyonetim/view/ManagerView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("aracyonetim.model.Vehicle Fleet Management System");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}