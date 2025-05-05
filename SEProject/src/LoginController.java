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

import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class LoginController {

    public LoginController() {
        DBConnection.initializeDB();
    }

    @FXML
    private TextField empID;
    @FXML
    private PasswordField empPass;
    @FXML
    private Label warningLabel;


    @FXML
    private void loginPressed(ActionEvent event) {
        // TODO: Handle login logic here
        System.out.println("Login button clicked");

        String userID = empID.getText();
        String password = empPass.getText();

        ResultSet resultSet;
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement("SELECT * FROM emp_login WHERE emp_id = ? AND emp_password = ?")) {

            statement.setString(1, userID);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                System.out.println("Login successful");
                Node n = (Node) event.getSource();
                Stage stage = (Stage) n.getScene().getWindow();
                stage.close();
                goToNextStage();
            }else{
                warningLabel.setText("ID or password wrong! Please try again");
            }


        } catch (SQLException e) {
            e.printStackTrace();
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
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ManagerView.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Vehicle Fleet Management System");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}