module aracyonetim {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.csv;

    opens aracyonetim.controller to javafx.fxml;
    opens aracyonetim.model to javafx.base;
    
    exports aracyonetim;
    exports aracyonetim.controller;
    exports aracyonetim.model;
} 