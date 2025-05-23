package aracyonetim;

import aracyonetim.db.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database
        DatabaseInitializer.initializeDatabase();
        
        // Load login view
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/aracyonetim/view/LoginView.fxml")));
        primaryStage.setTitle("Araç Yönetim Sistemi - Giriş");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
} 