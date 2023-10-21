package com.example.dsa;

import javafx.scene.control.Button;

public class Vertex extends Button {

    public Vertex(Double x, Double y){
        setLayoutX(x);
        setLayoutY(y);
        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(heightProperty().divide(-2));
    }
}
