import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FirmOfficial {
    private IntegerProperty empId;
    private IntegerProperty firmName;
    private StringProperty name;

    public FirmOfficial() {
        empId = new SimpleIntegerProperty();
        firmName = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
    }
    public IntegerProperty getEmpId() {
        return empId;
    }
    public void setEmpId(IntegerProperty empId) {
        this.empId = empId;
    }
    public IntegerProperty getFirmName() {
        return firmName;
    }
     public void setFirmName(IntegerProperty firmName) {
        this.firmName = firmName;
     }
     public StringProperty getName() {
        return name;
     }
     public void setName(StringProperty name) {
        this.name = name;
     }


}


