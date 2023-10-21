package com.example.dsa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Optional;

public class HelloController {
    public int color_counter = 0;
    public ArrayList<Button> b_list = new ArrayList<>();
    @FXML
    private Label welcomeText;
    @FXML
    AnchorPane graph;
    @FXML
    // when mouse is left clicked, a vertex would pop up
    public void onGraphPressed(MouseEvent mouseEvent) {
        if(mouseEvent.isPrimaryButtonDown()){
            graph.getChildren().add(createVertex(mouseEvent));
        }

    }

    // when the vertex is clicked.. you can do things...

    EventHandler<ActionEvent> vertex_action = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Button clickedButton = (Button) actionEvent.getSource();
            clickedButton.setStyle("-fx-background-color: red;");
        }
    };

    // this just create  buttons everytime mouse is clicked
    private Node createVertex(MouseEvent mouseEvent) {
        Button vertex = new Button();
        vertex.setLayoutX(mouseEvent.getX());
        vertex.setLayoutY(mouseEvent.getY());

        vertex.translateXProperty().bind(vertex.widthProperty().divide(-2));
        vertex.translateYProperty().bind(vertex.heightProperty().divide(-2));

        // TO BE EDIT...
        if (b_list.isEmpty()){ // if there are no vertices, create one with its name as A.
            vertex.setText("A");
            b_list.add(vertex);
        } else {
            // else if there are already vertices, increment it to other letters.
            ArrayList<Character> c_list = new ArrayList<>();
            for(Button btn : b_list){
                String s = btn.getText();
                char c = s.charAt(0);
                c_list.add(c);
            }
            char max = c_list.get(0);

            for (Character character : c_list) {
                if (max < character) {
                    max = character;
                }
            }
            max++;
            if (max == '['){
                max += 6;
            }
            String s = String.valueOf(max);
            vertex.setText(s);       // CHANGE LOCATION AS THE MOUSE DRAGS THE VERTEX
            vertex.setOnDragDetected(e -> DetectDrag(e, vertex));
            vertex.setOnMouseDragged(e -> WhenDragged(e, vertex));
            b_list.add(vertex);
        }
        ///



        // HIGHLIGHT THE VERTICES WHEN DOUBLE CLICKED
        vertex.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String style = vertex.getStyle();
                // ADD HIGHLIGHT IF DOUBLE CLICKED AND DO NOT HAVE HIGHLIGHTS YET
                if ((!style.contains("-fx-background-color: red;") && color_counter != 2)){
                    vertex.setStyle("-fx-background-color: red;");
                    color_counter += 1;
                }
                // REMOVE HIGHLIGHT IF YOU DOUBLE CLICKED THE ONE WITH HIGHLIGHT
                if (style.contains("-fx-background-color: red;")){
                    color_counter-=1;
                    vertex.setStyle(null);
                }

                // ONCE YOU HIGHLIGHTED 2 VERTEX..
                if (color_counter == 2){
                    color_counter = 0;

                    // GET THE TWO BUTTON IN THE WHOLE GRAPH THAT IS HIGHLIGHTED
                    ArrayList<Button> v_tmp = new ArrayList<>();
                    for (Button b: b_list){
                        String highlight = b.getStyle();
                        if (highlight.contains("-fx-background-color: red;")){
                            v_tmp.add(b);
                        }
                    }

                    //PROMPT MESSAGE WOULD POP UP IF YOU WANT TO PROCEED HIGHLIGHTING THEM
                    Alert prompt = new Alert(Alert.AlertType.CONFIRMATION);
                    prompt.setTitle("Title");

                    prompt.setContentText("Add edges between " + v_tmp.get(0).getText() + " and " + v_tmp.get(1).getText() + " ?");
                    Optional<ButtonType> result = prompt.showAndWait();

                    // do something
                    if (result.get() == ButtonType.OK) {
                        // TO-DO...
                        // CONNECT A LINE BETWEEN THE TWO VERTICES
                        createEdge(v_tmp.get(0), v_tmp.get(1));
                        System.out.println("*Do somethimg..*");
                    }

                    v_tmp.get(0).setStyle(null);
                    v_tmp.get(1).setStyle(null);



                }
            }
        });
        //BABAGUHIN
        // DELETE THE VERTEX
        vertex.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                String style = vertex.getStyle();
                if (style.contains("-fx-background-color: red;")){
                    vertex.setStyle(null);
                    color_counter -= 1;
                }
                graph.getChildren().remove(vertex);
            }
        });
        return vertex;
    }

    private void createEdge(Button v2, Button v1) {

    }


    private void DetectDrag(MouseEvent e, Button vertex) {
        vertex.toFront();
    }

    private void WhenDragged(MouseEvent e, Button vertex) {
        vertex.setLayoutX(vertex.getLayoutX() + e.getX() + vertex.getTranslateX());
        vertex.setLayoutY(vertex.getLayoutY() + e.getY() + vertex.getTranslateY());
    }

}