import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vehicle {
    private IntegerProperty id;
    private StringProperty licence;
    private StringProperty propertyType;
    private StringProperty model;
    private StringProperty brand;
    private IntegerProperty kmCount;
    private StringProperty isAssigned;

    public Vehicle(int id, String licence, String propertyType, String model, String brand, int kmCount, String isAssigned) {
        this.id = new SimpleIntegerProperty(id);                  // ← wraps primitive into JavaFX property
        this.licence = new SimpleStringProperty(licence);
        this.propertyType = new SimpleStringProperty(propertyType);
        this.model = new SimpleStringProperty(model);
        this.brand = new SimpleStringProperty(brand);
        this.kmCount = new SimpleIntegerProperty(kmCount);
        this.isAssigned = new SimpleStringProperty(isAssigned);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getLicence() {
        return licence.get();
    }

    public StringProperty licenceProperty() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence.set(licence);
    }

    public String getPropertyType() {
        return propertyType.get();
    }

    public StringProperty propertyTypeProperty() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType.set(propertyType);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getBrand() {
        return brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public int getKmCount() {
        return kmCount.get();
    }

    public IntegerProperty kmCountProperty() {
        return kmCount;
    }

    public void setKmCount(int kmCount) {
        this.kmCount.set(kmCount);
    }

    public String getIsAssigned() {
        return isAssigned.get();
    }

    public StringProperty isAssignedProperty() {
        return isAssigned;
    }

    public void setIsAssigned(String isAssigned) {
        this.isAssigned.set(isAssigned);
    }
}
