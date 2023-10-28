package com.example.dsa;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Optional;

public class HelloController {

    // dont mind this, just to limit two highights when creating a connection.
    public int color_counter = 0;
    //vertex_button_array is where all vertex (button) are stored.
    public ArrayList<Button> vertex_button_array = new ArrayList<>();

    //public Graph graph_backend;

    //edges_button_array is where all edges (line) are stored.
    public ArrayList<Line> edges_button_array = new ArrayList<>();

    @FXML
    private Label welcomeText;
    @FXML
    AnchorPane graph;
    @FXML


    //CREATE A FUNCTION TO PROMPT EITHER WEIGHT OR NON WEIGHTED
    /*public void weightedChecker(){
        //EXMAPLE  ONLY
        graph_backend = new Graph(true, false);
    }*/

    // when mouse is left clicked, a vertex would pop up
    public void onGraphPressed(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            Node clickedNode = (Node) mouseEvent.getTarget();
            if (!(clickedNode instanceof Button || clickedNode instanceof Line || clickedNode instanceof Circle)) {
                graph.getChildren().add(createVertex(mouseEvent));
            }
        }

    }

    // this just create  buttons everytime mouse is clicked
    private Node createVertex(MouseEvent mouseEvent) {
        Button vertex = new Button();
        vertex.setLayoutX(mouseEvent.getX());
        vertex.setLayoutY(mouseEvent.getY());
        vertex.getStyleClass().add("UIvertex");

        vertex.translateXProperty().bind(vertex.widthProperty().divide(-2));
        vertex.translateYProperty().bind(vertex.heightProperty().divide(-2));

        // TO BE EDIT...
        if (vertex_button_array.isEmpty()){ // if there are no vertices, create one with its name as A.
            vertex.setText("A");
        }
        else {
            // else if there are already vertices, increment it to other letters.
            ArrayList<Character> c_list = new ArrayList<>();

            for(Button btn : vertex_button_array){
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
            vertex.setText(s);

        }

        // CHANGE LOCATION AS THE MOUSE DRAGS THE VERTEx
        vertex_button_array.add(vertex);

        /*
        // ADD THE NAME OF THE VERTEX BUTTON TO THE GRAPH CLASS FOR BACKEND PROCESS
        graph_backend.addVertex(vertex.getText());
         */

        vertex.setOnDragDetected(e -> DetectDrag(e, vertex));
        vertex.setOnMouseDragged(e -> WhenDragged(e, vertex));



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
                    for (Button b: vertex_button_array){
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

                    if (result.get() == ButtonType.OK) {
                        // CONNECT A LINE BETWEEN THE TWO VERTICES
                        createEdge(v_tmp.get(0), v_tmp.get(1));
                    }

                    v_tmp.get(0).setStyle(null);
                    v_tmp.get(1).setStyle(null);



                }
            }
        });
        // DELETE THE VERTEX
        vertex.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                String style = vertex.getStyle();
                if (style.contains("-fx-background-color: red;")){
                    vertex.setStyle(null);
                    color_counter -= 1;
                }
                graph.getChildren().remove(vertex);

                /*
                for (Vertex tmp: graph_backend.vertices){
                    if (tmp.data.equals(vertex.getText()){

                       graph_backend.removeVertex(tmp);
                    }
                }
                 */
            }
        });
        return vertex;
    }
    // CREATE AN EDGE BETWEEN TWO VERTEX
    private void createEdge(Button v0, Button v1) {
        double[] start = LineScaler(v0.getLayoutX(),v0.getLayoutY(), v1.getLayoutX(), v1.getLayoutY());
        double[] end = LineScaler(v1.getLayoutX(), v1.getLayoutY(),v0.getLayoutX(),v0.getLayoutY() );

        double x3 = start[0];
        double y3 = start[1];
        double x4 = end[0];
        double y4 = end[1];

        Line line = new Line(x3, y3, x4, y4);
        graph.getChildren().add(line);


        // when hovered, a visual dot would appear through starting and end points
        Circle startDot;
        Circle endDot;
        startDot = new Circle(5); // Adjust the size as needed
        startDot.getStyleClass().add("UIcircle");
        startDot.setRadius(10.0f);

        endDot = new Circle(5); // Adjust the size as needed
        endDot.setRadius(10.0f);
        endDot.getStyleClass().add("UIcircle");

        graph.getChildren().add(startDot);
        graph.getChildren().add(endDot);

        startDot.setVisible(false);
        endDot.setVisible(false);

        startDot.setLayoutX(line.getStartX());
        startDot.setLayoutY(line.getStartY());

        endDot.setLayoutX(line.getEndX());
        endDot.setLayoutY(line.getEndY());

        //move an edge to another vertex
        startDot.setOnMouseDragged(e -> WhenEdgePointDragged(e, startDot, line, "start"));
        endDot.setOnMouseDragged(e -> WhenEdgePointDragged(e, endDot, line, "end"));

        // fix visuals, line must be behind the vertex
        startDot.setOnDragDetected(e-> DetectEdgePointDrag(e, startDot, line));
        endDot.setOnDragDetected(e -> DetectEdgePointDrag(e, endDot, line));

        // REMOVE EDGE or LINE
        line.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                graph.getChildren().remove(line);
            }
        });


        String default_width = "-fx-stroke-width: 5;";
        line.setStyle(default_width);

        // change color when mouse is hovered in the line
        String highlightStyle = "-fx-stroke: blue;" + default_width;

        // when hovered in edge
        line.setOnMouseEntered(event -> {
            startDot.setVisible(true);
            startDot.toFront();
            endDot.toFront();
            endDot.setVisible(true);
            line.setStyle(highlightStyle);
        });

        // when exit hovering the edge
        line.setOnMouseExited(event -> {
            startDot.setVisible(false);
            endDot.setVisible(false);
            line.setStyle(default_width);
        });

        // when clicking the starting/endpoint
        startDot.setOnMouseClicked(event -> {
            System.out.println("Clicked on start dot.");
        });

        endDot.setOnMouseClicked(event -> {
            System.out.println("Clicked on end dot.");
        });


    }

    // visual fix for vertex, dont mind
    private void DetectDrag(MouseEvent e, Button vertex) {
        vertex.toFront();
    }

    // WHEN THE VERTEX IS DRAGGED
    private void WhenDragged(MouseEvent e, Button vertex) {
        vertex.setLayoutX(vertex.getLayoutX() + e.getX() + vertex.getTranslateX());
        vertex.setLayoutY(vertex.getLayoutY() + e.getY() + vertex.getTranslateY());

        // add come code where we find the edges of the vertex, if an edge is found, also include that to be dragged
        // BACKEND SECTION
        // add function findEdges(Vertex a); (backend)
    }

    // SET COORDINATES OF THE NEWLY CREATED EDGE SO THAT THE EDGE WONT OBSTRUCT THE VIEW OF THE VERTEX
    private double[] LineScaler(double x1, double y1, double x2, double y2){
        double theta = Math.atan2(y2-y1, x2-x1);
        return new double[] {
                x1 + Math.cos(theta) * 22,
                y1 + Math.sin(theta) * 22
        };
    }


    private void WhenEdgePointDragged(MouseEvent e, Circle c, Line line, String loc){
        c.setLayoutX(c.getLayoutX() + e.getX() + c.getTranslateX());
        c.setLayoutY(c.getLayoutY() + e.getY() + c.getTranslateY());
        if (loc.equals("start")){
            line.setStartX(c.getLayoutX());
            line.setStartY(c.getLayoutY());
        } else if (loc.equals("end")) {
            line.setEndX(c.getLayoutX());
            line.setEndY(c.getLayoutY());
        }
    }

    //visual fix for edges, dont mind
    private void DetectEdgePointDrag(MouseEvent e, Circle c, Line line) {line.toBack();}
}