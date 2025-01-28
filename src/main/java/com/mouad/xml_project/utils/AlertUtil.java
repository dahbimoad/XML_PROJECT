package com.mouad.xml_project.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtil {

    public static void showInfoAlert(String title, String content) {
        showAlert(AlertType.INFORMATION, title, content);
    }

    public static void showErrorAlert(String title, String content) {
        showAlert(AlertType.ERROR, title, content);
    }

    public static void showWarningAlert(String title, String content) {
        showAlert(AlertType.WARNING, title, content);
    }

    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}