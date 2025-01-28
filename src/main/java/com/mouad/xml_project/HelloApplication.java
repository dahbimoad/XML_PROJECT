package com.mouad.xml_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/mouad/xml_project/fxml/main-view.fxml"));
            primaryStage.setTitle("Gestion de Scolarité - GINF2");
            primaryStage.setScene(new Scene(root, 800, 600));

            // Create output directories if they don't exist
            createOutputDirectories();

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createOutputDirectories() {
        String[] dirs = {
            "output/html",
            "output/pdf",
            "output/excel"
        };

        for (String dir : dirs) {
            new File(dir).mkdirs();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}