package aracyonetim.controller;

import aracyonetim.dao.KullaniciDAO;
import aracyonetim.db.DBConnection;
import aracyonetim.model.Kullanici;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField phoneField;
    @FXML private TextField companyField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label errorLabel;

    private KullaniciDAO kullaniciDAO;

    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            kullaniciDAO = new KullaniciDAO(conn);
            
            // Set up role dropdown
            roleComboBox.setItems(FXCollections.observableArrayList(
                "CALISAN", 
                "FIRMA_YETKILISI",
                "DIS_FIRMA_YETKILISI"
            ));
            roleComboBox.setValue("CALISAN"); // Default value
            
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database connection error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        // Clear previous errors
        errorLabel.setText("");
        
        // Validate input fields
        if (!validateFields()) {
            return;
        }
        
        try {
            // Create a new user object
            Kullanici kullanici = new Kullanici();
            kullanici.setAd(firstNameField.getText().trim());
            kullanici.setSoyad(lastNameField.getText().trim());
            kullanici.setEmail(emailField.getText().trim());
            kullanici.setSifre(passwordField.getText());
            kullanici.setTelefon(phoneField.getText().trim());
            kullanici.setFirma(companyField.getText().trim());
            kullanici.setRol(roleComboBox.getValue());
            kullanici.setAktif(true);
            
            // Save to database
            kullaniciDAO.kullaniciEkle(kullanici);
            
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your account has been created successfully. You can now log in.");
            alert.showAndWait();
            
            // Return to login screen
            returnToLogin(event);
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                showError("A user with this email already exists.");
            } else {
                showError("Error creating account: " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error returning to login screen: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            returnToLogin(event);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error returning to login screen: " + e.getMessage());
        }
    }
    
    private boolean validateFields() {
        // Check for empty fields
        if (isEmpty(firstNameField.getText()) || 
            isEmpty(lastNameField.getText()) || 
            isEmpty(emailField.getText()) || 
            isEmpty(passwordField.getText()) || 
            isEmpty(confirmPasswordField.getText()) ||
            isEmpty(companyField.getText()) ||
            roleComboBox.getValue() == null) {
            
            showError("All fields are required.");
            return false;
        }
        
        // Validate email format
        if (!isValidEmail(emailField.getText().trim())) {
            showError("Please enter a valid email address.");
            return false;
        }
        
        // Check password length
        if (passwordField.getText().length() < 6) {
            showError("Password must be at least 6 characters long.");
            return false;
        }
        
        // Check if passwords match
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Passwords do not match.");
            return false;
        }
        
        return true;
    }
    
    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
    }
    
    private void returnToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/aracyonetim/view/LoginView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Araç Yönetim Sistemi - Giriş");
        stage.setScene(new Scene(root));
        stage.show();
    }
} 