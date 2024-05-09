package com.example.dsa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class mainApp extends Application {
    /**
     *<h1 style="font-weight:bold;">Main Application</h1>
     * <p style="font-family:Calibri; font-size: 15px;">mainApp.java operates the fxml file of the mainController.java. This is where the application is supposed
     * to be operated in order to start the program.</p>
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mainApp.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),778, 477);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Graph Maker");
        stage.setScene(scene);
        stage.getIcons().add(new Image("/graphmaker_logo.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}