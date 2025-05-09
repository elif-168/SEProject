package aracyonetim.controller;

import aracyonetim.db.DBConnection;
import aracyonetim.model.Arac;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManagerController {

    //aracyonetim.model.Vehicle property type 0 == > bizim uygulamadan kiralik, 1 ==> kendi araclari

    @FXML
    private TableView<Arac> table = new TableView<>();

    public ManagerController() {

    }

    @FXML private TableColumn<Arac, Number> idColumn;
    @FXML private TableColumn<Arac, String> modelColumn;
    @FXML private TableColumn<Arac, String> brandColumn;
    @FXML private TableColumn<Arac, String> assignmentColumn;
    @FXML private TableColumn<Arac, String> propertyColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());
        modelColumn.setCellValueFactory(data -> data.getValue().modelProperty());
        brandColumn.setCellValueFactory(data -> data.getValue().brandProperty());
        assignmentColumn.setCellValueFactory(data-> data.getValue().isAssignedProperty());
        propertyColumn.setCellValueFactory(data -> data.getValue().propertyTypeProperty());

        ObservableList<Arac> aracs = FXCollections.observableArrayList();

        try (Statement stmnt = DBConnection.getConnection().createStatement()) {
            ResultSet resultSet = stmnt.executeQuery("SELECT * FROM Arac");

            while (resultSet.next()) {
                Arac v = new Arac(
                        resultSet.getInt("aracId"),
                        resultSet.getString("plaka"),
                        resultSet.getBoolean("kiralik"),
                        resultSet.getString("model"),
                        resultSet.getString("marka"),
                        resultSet.getInt("km"),
                        ((resultSet.getInt("isAssigned") == 1 )? "assigned" : "free")
                );
                aracs.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(aracs);
    }


}
