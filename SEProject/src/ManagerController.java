import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManagerController {

    //Vehicle property type 0 == > bizim uygulamadan kiralik, 1 ==> kendi araclari

    @FXML
    private TableView<Vehicle> table = new TableView<>();

    public ManagerController() {

    //elif
    }

    @FXML private TableColumn<Vehicle, Number> idColumn;
    @FXML private TableColumn<Vehicle, String> modelColumn;
    @FXML private TableColumn<Vehicle, String> brandColumn;
    @FXML private TableColumn<Vehicle, String> assignmentColumn;
    @FXML private TableColumn<Vehicle, String> propertyColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());
        modelColumn.setCellValueFactory(data -> data.getValue().modelProperty());
        brandColumn.setCellValueFactory(data -> data.getValue().brandProperty());
        assignmentColumn.setCellValueFactory(data-> data.getValue().isAssignedProperty());
        propertyColumn.setCellValueFactory(data -> data.getValue().propertyTypeProperty());

        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();

        try (Statement stmnt = DBConnection.getConnection().createStatement()) {
            ResultSet resultSet = stmnt.executeQuery("SELECT * FROM vehicles");

            while (resultSet.next()) {
                Vehicle v = new Vehicle(
                        resultSet.getInt("vehicle_id"),
                        resultSet.getString("licence_plate"),
                        ((resultSet.getInt("property_type") == 1) ? "rental" : "owned"),
                        resultSet.getString("model"),
                        resultSet.getString("brand"),
                        resultSet.getInt("km_count"),
                        ((resultSet.getInt("isAssigned") == 1 )? "assigned" : "free")
                );
                vehicles.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(vehicles);
    }


}
