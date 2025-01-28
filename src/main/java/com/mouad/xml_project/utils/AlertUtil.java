package com.mouad.xml_project.utils;

import javafx.scene.control.Alert;

public class AlertUtil {

    private AlertUtil() {
    }

    public static void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}