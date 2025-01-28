module com.mouad.xml_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.xml;
    requires fop.core;

    opens com.mouad.xml_project.controllers to javafx.fxml;
    opens com.mouad.xml_project to javafx.fxml;
    exports com.mouad.xml_project;
}
