package com.example.dsa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Instruction {
    @FXML
    private Button btn_exit;
    @FXML
    BorderPane borderpane;

    public void exit(MouseEvent mouseEvent) {
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    Double x = 0.0;
    Double y = 0.0;
    @FXML
    void BorderDrag(MouseEvent event) {
        Stage stage = (Stage) borderpane.getScene().getWindow();
        stage.setY(event.getScreenY() - y);
        stage.setX(event.getScreenX() - x);
    }

    @FXML
    void BorderPress(MouseEvent event){
        x = event.getSceneX();
        y = event.getSceneY();
    }
}
