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
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Optional;

public class HelloController {

    // dont mind this, just to limit two highights when creating a connection.
    public int color_counter = 0;
    //vertex_button_array is where all vertex (button) are stored.
    public ArrayList<VxButtonObj> vertex_button_array = new ArrayList<>();

    Graph graph_backend = new Graph(false, false);

    //edges_button_array is where all edges (line) are stored.
    public ArrayList<Line> edges_button_array = new ArrayList<>();

    @FXML
    private Label welcomeText;
    @FXML
    AnchorPane graph;
    @FXML



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
        VxButtonObj vertex_button = new VxButtonObj();
        vertex_button.setLayoutX(mouseEvent.getX());
        vertex_button.setLayoutY(mouseEvent.getY());
        vertex_button.getStyleClass().add("UIvertex");
        vertex_button.translateXProperty().bind(vertex_button.widthProperty().divide(-2));
        vertex_button.translateYProperty().bind(vertex_button.heightProperty().divide(-2));


        if (vertex_button_array.isEmpty()){ // if there are no vertices, create one with its name as A.
            vertex_button.setText("A");
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
            vertex_button.setText(s);
        }

        // CHANGE LOCATION AS THE MOUSE DRAGS THE VERTEx
        vertex_button_array.add(vertex_button);

        vertex_button.setOnDragDetected(e -> DetectDrag(e, vertex_button));


        vertex_button.setOnMouseDragged(e -> WhenDragged(e, vertex_button));



        // HIGHLIGHT THE VERTICES WHEN DOUBLE CLICKED
        vertex_button.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String style = vertex_button.getStyle();

                // ADD HIGHLIGHT IF DOUBLE CLICKED AND DO NOT HAVE HIGHLIGHTS YET
                if ((!style.contains("-fx-background-color: red;") && color_counter != 2)){
                    vertex_button.setStyle("-fx-background-color: red;");
                    color_counter += 1;
                }

                // REMOVE HIGHLIGHT IF YOU DOUBLE CLICKED THE ONE WITH HIGHLIGHT
                if (style.contains("-fx-background-color: red;")){
                    color_counter-=1;
                    vertex_button.setStyle(null);
                }

                // ONCE YOU HIGHLIGHTED 2 VERTEX..
                if (color_counter == 2){
                    color_counter = 0;

                    // GET THE TWO BUTTON IN THE WHOLE GRAPH THAT IS HIGHLIGHTED
                    ArrayList<VxButtonObj> v_tmp = new ArrayList<>();
                    for (VxButtonObj b: vertex_button_array){
                        String highlight = b.getStyle();
                        if (highlight.contains("-fx-background-color: red;")){
                            v_tmp.add(b);
                        }
                    }

                    // CONNECT A LINE BETWEEN THE TWO VERTICES
                    createEdge(v_tmp.get(0), v_tmp.get(1));
                    v_tmp.get(0).setStyle(null);
                    v_tmp.get(1).setStyle(null);
                }
            }
        });

        // DELETE THE VERTEX // ALSO DELETE ITS EDGES
        vertex_button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                String style = vertex_button.getStyle();
                if (style.contains("-fx-background-color: red;")){
                    vertex_button.setStyle(null);
                    color_counter -= 1;
                }

                //must also remove the edge simultaneously in the backend and frontend
                    // to remove in front
                for (Line tmp: edges_button_array){

                    System.out.println("THE LINE IS: " + tmp.getId());
                    // this if statement visually removes the edges if the id of one of the lines is present on the
                    // id of the vertex
                    if (tmp.getId().contains(vertex_button.getText())){
                        System.out.println("REMOVED: " + tmp.getId());
                        System.out.println("BECAUSE: " + tmp.getId() + " contains " + vertex_button.getText() );
                        graph.getChildren().remove(tmp);
                    }
                }
                    // remove in backend
                for (Vertex vertex_tmp: graph_backend.getVertices()){
                    vertex_tmp.removeEdge(vertex_button.getVertexObj());
                }

                //then remove in the whole graph
                graph_backend.removeVertex(vertex_button.getVertexObj());


                // remove in front end
                graph.getChildren().remove(vertex_button);

                //update
                graph_backend.print();
            }
        });

        // gumana toh
        vertex_button.setVertexObj(graph_backend.addVertex(vertex_button.getText()));
        return vertex_button;
    }


    // CREATE AN EDGE BETWEEN TWO VERTEX
    private void createEdge(VxButtonObj v0, VxButtonObj v1) {

        // Coordinate of vertex v0 (A) and v1(B)
        String edgename = v0.getText()+v1.getText();
        String rev_edgename = v1.getText() + v0.getText();

        Boolean has_duplicate = false;
        for (Line tmp: edges_button_array){
            String s_tmp = tmp.getId();
            if (s_tmp.equals(edgename)){
                System.out.println("Edge already exist!");
                has_duplicate = true;
            }
        }

        if (!has_duplicate){

            Line line = new Line(v0.getLayoutX(),v0.getLayoutY(), v1.getLayoutX(), v1.getLayoutY());
            line.setId(edgename);

           Line line2 = new Line(v0.getLayoutX(),v0.getLayoutY(), v1.getLayoutX(), v1.getLayoutY());
            line2.setId(rev_edgename);

            System.out.println(line.getId());
            graph.getChildren().add(line);

            edges_button_array.add(line);
            edges_button_array.add(line2);

            line.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    graph.getChildren().remove(line);
                    edges_button_array.remove(line);
                }
            });
            String default_width = "-fx-stroke-width: 5;";
            line.setStyle(default_width);

            graph_backend.addEdge(v0.getVertexObj(), v1.getVertexObj(), null);
            graph_backend.print();
            line.toBack();
        } else {
            System.out.println("No new edge formed");
        }
    }

    // visual fix for vertex, dont mind
    private void DetectDrag(MouseEvent e, VxButtonObj vertex) {
        vertex.toFront();
    }

    // WHEN THE VERTEX IS DRAGGED
    private void WhenDragged(MouseEvent e, VxButtonObj vertex) {
        // drag the button or the vertex along the position of the mouse
        vertex.setLayoutX(vertex.getLayoutX() + e.getX() + vertex.getTranslateX());
        vertex.setLayoutY(vertex.getLayoutY() + e.getY() + vertex.getTranslateY());

        double newX = vertex.getLayoutX() + e.getX() + vertex.getTranslateX();
        double newY = vertex.getLayoutY() + e.getY() + vertex.getTranslateY();

        // call first the edges of the vertex
        for(Edge edge_tmp: vertex.getVertexObj().getEdges()){
            // to find the specific line in the frontend from the backend
            for (Line line_tmp: edges_button_array){
                // if found the line
                if (line_tmp.getId().contains(edge_tmp.getStartV().getData()) && line_tmp.getId().contains(edge_tmp.getEndV().getData())){
                    String k = String.valueOf(line_tmp.getId().charAt(1));
                    String l = String.valueOf(line_tmp.getId().charAt(0));
                    // if the vertex you are moving has the endpoint of the line
                    if(vertex.getText().equals(k)){
                        line_tmp.setEndX(newX);
                        line_tmp.setEndY(newY);
                    } else if (vertex.getText().equals(l)) { // but if the vertex you are moving has the startingpoint of the line
                        line_tmp.setStartX(newX);
                        line_tmp.setStartY(newY);
                    }
                }
            }
        }
    }

    private void WhenEdgePointDragged(MouseEvent e, Circle c, Line line, String loc){
        c.setLayoutX(c.getLayoutX() + e.getX() + c.getTranslateX());
        c.setLayoutY(c.getLayoutY() + e.getY() + c.getTranslateY());

        double newX = c.getLayoutX() + e.getX() + c.getTranslateX();
        double newY = c.getLayoutY() + e.getY() + c.getTranslateY();

        double snappingRange = 20; // Adjust as needed

        if (loc.equals("start")){
            line.setStartX(newX);
            line.setStartY(newY);
            //snapToVertex(newX, newY, line, snappingRange);
        } else if (loc.equals("end")) {
            line.setEndX(newX);
            line.setEndY(newY);
            //snapToVertex(newX, newY, line, snappingRange);
        }
    }

    //visual fix for edges, dont mind
    private void DetectEdgePointDrag(MouseEvent e, Circle c, Line line) {line.toBack();         }

}