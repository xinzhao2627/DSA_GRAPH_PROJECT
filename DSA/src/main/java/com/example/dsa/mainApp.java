package com.example.dsa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
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