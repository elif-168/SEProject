import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.sql.*;

public class LoginController {

    public LoginController() {
        DBConnection.initializeDB();
    }



    @FXML
    private void login(ActionEvent event) {
        // TODO: Handle login logic here
        System.out.println("Login button clicked");

        ResultSet resultSet;
        try (Statement statement = DBConnection.getConnection().createStatement()) {

            resultSet = statement.executeQuery("select * from emp_login");


            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println("Table structure:");
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", metaData.getColumnName(i));
            }
            System.out.println();

            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s", resultSet.getString(i));
                }
                System.out.println();
            }

            if (!hasResults) {
                System.out.println("No records found in emp_login table");
            }

            while (resultSet.next()) {
                System.out.println("*****");
                System.out.println(resultSet.getString("emp_password"));
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
}