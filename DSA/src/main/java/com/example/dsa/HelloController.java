package com.example.dsa;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    // dont mind this, just to limit two highights when creating a connection.
    public int color_counter = 0;

    //vertex_button_array is where all vertex (button) are stored.
    public ArrayList<VxButtonObj> vertex_button_array = new ArrayList<>();

    Graph graph_backend = new Graph(false, false);

    //edges_button_array is where all edges (line) are stored.
    public ArrayList<Line> edges_button_array = new ArrayList<>();

    ShowHamilCircuit showHamilCircuit;
    ShowEulerCirc showEulerCirc;
    ShowHamilPath showHamilPath;

    DataModel dataModel;


    @FXML
    private BorderPane borderpane;

    @FXML
    private Button About;

    @FXML
    private Button Djikstra;

    @FXML
    private Button EulerianCircuit;

    @FXML
    private Button EulerianPath;

    @FXML
    private Button HamiltonianCircuit;


    @FXML
    private Button HamiltonianPath;

    @FXML
    private Button Instructions;

    @FXML
    private Label welcomeText;
    @FXML
    AnchorPane graph;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private ImageView exit;

    @FXML
    private AnchorPane slider;

    @FXML
    private Button implementer;

    Parent root;

    @FXML
    private Button reset_btn;


    @FXML
    void reset(MouseEvent event) {
        System.out.println("reset clicked");
        graph_backend = new Graph(false, false);
        graph.getChildren().clear();
        edges_button_array.clear();
        vertex_button_array.clear();
        graph.setDisable(false);
    }


    public void setHelloControllerData (AnchorPane graph_frontend, Graph graph_backend, ArrayList<VxButtonObj> vertex_button_array, ArrayList<Line> edges_button_array ){
        this.graph.getChildren().addAll(graph_frontend.getChildren());
        this.vertex_button_array = vertex_button_array;
        this.edges_button_array = edges_button_array;
        this.graph_backend = graph_backend;

        this.graph.toFront();

//        for (VxButtonObj tmp: this.vertex_button_array){
//            System.out.println("vertex found: " + tmp.getText());
//            tmp.setVisible(true);
//        }
//
//        for (Line tmp: this.edges_button_array){
//            System.out.println("line found " + tmp.getId());
//            tmp.setVisible(true);
//        }
        graph.setDisable(true);
    }


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
//                for (Line tmp: edges_button_array) {
//
//                    System.out.println("THE LINE IS: " + tmp.getId());
//
//                    // this if statement visually removes the edges if the id of one of the lines is present on the
//                    // id of the vertex
//                    if (tmp.getId().contains(vertex_button.getText())) {
//                        System.out.println("REMOVED: " + tmp.getId());
//                        System.out.println("BECAUSE: " + tmp.getId() + " contains " + vertex_button.getText());
//                        graph.getChildren().remove(tmp);
//                    }
//                }
                Iterator<Line> it = edges_button_array.iterator();
                while (it.hasNext()){
                    Line tmp = it.next();
                    System.out.println("THE LINE IS: " + tmp.getId());

                    // this if statement visually removes the edges if the id of one of the lines is present on the
                    // id of the vertex
                    if (tmp.getId().contains(vertex_button.getText())) {
                        System.out.println("REMOVED: " + tmp.getId());
                        System.out.println("BECAUSE: " + tmp.getId() + " contains " + vertex_button.getText());
                        graph.getChildren().remove(tmp);
                        it.remove();
                    }
                }

                    // remove in backend
                for (Vertex vertex_tmp: graph_backend.getVertices()){
                    vertex_tmp.removeEdge(vertex_button.getVertexObj());
                }

                //then remove in the whole graph
                graph_backend.removeVertex(vertex_button.getVertexObj());

                //remove in a stored arraylist of buttons
                vertex_button_array.remove(vertex_button);

                // remove in front end
                graph.getChildren().remove(vertex_button);

                //update
                graph_backend.print();
            }
        });

        // gumana toh BACKEND
        Vertex v = graph_backend.addVertex(vertex_button.getText());
        vertex_button.setVertexObj(v);
        //graph_backend.addVertex(vertex_button.getText());
        return vertex_button;
    }


    // CREATE AN EDGE BETWEEN TWO VERTEX
    private void createEdge(VxButtonObj v0, VxButtonObj v1) {

        // Coordinate of vertex v0 (A) and v1(B)
        String edgename = v0.getText()+v1.getText();

        //check if edge already exist between v0 and v1
        Boolean has_duplicate = false;
        for (Line tmp: edges_button_array){
            String s_tmp = tmp.getId();
            if (s_tmp.equals(edgename)){
                System.out.println("Edge already exist!");
                has_duplicate = true;
            }
        }

        // if there is not then go make an edge
        if (!has_duplicate){

            Line line = new Line(v0.getLayoutX(),v0.getLayoutY(), v1.getLayoutX(), v1.getLayoutY());
            line.setId(edgename);

            System.out.println(line.getId());
            graph.getChildren().add(line);
            edges_button_array.add(line);


            //REMOVE edge
            line.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    graph.getChildren().remove(line);
                    edges_button_array.remove(line);
                    graph_backend.removeEdge(v0.getVertexObj(), v1.getVertexObj());
                }
            });
            String default_width = "-fx-stroke-width: 2.7;";
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
            System.out.println("edge_tmp");
            // to find the specific line in the frontend from the backend
            for (Line line_tmp: edges_button_array){
                // if found the line
                if (line_tmp.getId().contains(edge_tmp.getStartV().getData()) && line_tmp.getId().contains(edge_tmp.getEndV().getData())){
                    System.out.println("LINE FOUND");
                    String k = String.valueOf(line_tmp.getId().charAt(1));
                    String l = String.valueOf(line_tmp.getId().charAt(0));
                    // if the vertex you are moving has the endpoint of the line
                    if(vertex.getText().equals(k)){
                        System.out.println("edge movving");
                        line_tmp.setEndX(newX);
                        line_tmp.setEndY(newY);
                    } else if (vertex.getText().equals(l)) { // but if the vertex you are moving has the startingpoint of the line
                        System.out.println("Edge moving");
                        line_tmp.setStartX(newX);
                        line_tmp.setStartY(newY);
                    }
                }
            }
        }
    }

    // initialize all variables in the fxml file
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This is just when you click that power off button
        exit.setOnMouseClicked(event -> System.exit(0));
        MenuBack.setVisible(false);

        slider.setTranslateX(180);

        Menu.setOnMouseClicked(event -> {
            System.out.println("menu is clicked");
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            // the slider variable is the side menu itself that contains all options (Euler, Hamil, Djiks, etc)
            slider.setTranslateX(176);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        // hide the menu
        MenuBack.setOnMouseClicked(event -> {
            System.out.println("closemenu is clicked");
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(slider);

            slide.setToX(180);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        });

    }

    // CALL HAMILTONIAN Circuit
    public void HamilCircScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowHamilCircuit.fxml"));
        root = fxmlLoader.load();

        Stage old_stage = (Stage) HamiltonianCircuit.getScene().getWindow();
        showHamilCircuit = fxmlLoader.getController();
        showHamilCircuit.setHamilCircData(vertex_button_array, graph_backend, graph, edges_button_array);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        //''back button''
        old_stage.close();
    }

    // CALL EULERIAN CIRCUIT
    public void EulerCircScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowEulerCirc.fxml"));
        root = fxmlLoader.load();

        Stage old_stage = (Stage) EulerianCircuit.getScene().getWindow();
        showEulerCirc = fxmlLoader.getController();
        showEulerCirc.setEulerCircData(vertex_button_array, graph_backend, graph , edges_button_array, old_stage);


        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();


        //''back button''
        old_stage.close();

    }

    // CALL HAMILTONIAN PATH
    @FXML
    void HamilPathScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowHamilPath.fxml"));
        root = fxmlLoader.load();

        Stage old_stage = (Stage) HamiltonianPath.getScene().getWindow();
        showHamilPath = fxmlLoader.getController();
        showHamilPath.setHamilPathData(vertex_button_array, graph_backend, graph,edges_button_array);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        old_stage.close();
    }


    // CALL ABOUT
    public void AboutScene (ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
        root = fxmlLoader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        Stage old_stage = (Stage) About.getScene().getWindow();
        //''back button''
        old_stage.hide();
    }

    // CALL INSTRUCTION
    public void InstructionScene (ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Instruction.fxml"));
        root = fxmlLoader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        Stage old_stage = (Stage) Instructions.getScene().getWindow();
        //''back button''
        old_stage.close();
    }

    // TO EXTRACT THE OUTCOME
    @FXML
    void implementDataModel(ActionEvent event) {

        // if a path/circuit is clicked previously...
        dataModel = showHamilCircuit.getDataModel();
        //dataModel is contains all vertex that is the hamiltonian path.
    }


    // TO MOVE THE APPLICATION
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