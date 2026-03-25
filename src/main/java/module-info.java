module com.clinica.aauca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.jensd.fx.glyphs.fontawesome; // Dependencia de FontAwesomeFX
    requires jbcrypt; // Dependencia de BCrypt

    opens com.clinica.aauca to javafx.fxml;
    opens com.clinica.aauca.controller to javafx.fxml;
    opens com.clinica.aauca.model to javafx.base; // Para permitir el acceso a las propiedades en TableViews, etc.

    exports com.clinica.aauca;
    exports com.clinica.aauca.controller;
    exports com.clinica.aauca.model;
    exports com.clinica.aauca.dao;
}
