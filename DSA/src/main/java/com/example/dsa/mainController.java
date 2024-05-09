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

public class mainController implements Initializable {
    /**
     * <h2 style="font-weight:bold;">Variable declaration</h2>
     * <p style="font-family:Calibri; font-size: 15px;">JavaFX and Object components are initialized here.</p>
     */

    //limit two highights when creating a connection.
    public int color_counter = 0;
    //vertex_button_array is where all vertex (button) are stored.
    public ArrayList<VxButtonObj> vertex_button_array = new ArrayList<>();
    public ArrayList<Character> deleted_vertex_names = new ArrayList<>();
    public ArrayList<Character> vertex_names = new ArrayList<>();
    Graph graph_backend = new Graph(false, false);
    //edges_button_array is where all edges (line) are stored.
    public ArrayList<Line> edges_button_array = new ArrayList<>();
    ShowHamilCircuit showHamilCircuit;
    ShowEulerCirc showEulerCirc;
    ShowHamilPath showHamilPath;
    About   about;
    Instruction instruction;
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


    /**
     * <h2>Function 1: Reset Button</h2>
     * <p style="font-family:Calibri; font-size: 15px;">
     *     Upon clicking the reset button in the UI, the function empties the collection of vertices and edges in the backend and clears the visual representation in the frontend</p>
     */
    @FXML
    void reset(MouseEvent event) {
        graph_backend = new Graph(false, false);
        graph.getChildren().clear();
        edges_button_array.clear();
        vertex_button_array.clear();
        vertex_names.clear();
        deleted_vertex_names.clear();
        color_counter = 0;
        graph.setDisable(false);
    }

    /**
     * <h2>Function 2: Graph transfer between scenes</h2>
     * <p style="font-family:Calibri; font-size: 15px;">
     *     Transfers the graph data between scenes. If a user
     *     goes to other scenes. The frontend Graph data would be transferred to the selected
     *     scene. This function would then be used when the other scenes wants to go back to the mainController (this scene, Graph making interface).
     *     The graph is also disabled to preserve the data, disabling the user to make any changes.
     * </p>
     *
     * <br>
     * <small>Scenes are the different pages of the UI</small>
     */
    public void setMainControllerData(AnchorPane graph_frontend, Graph graph_backend, ArrayList<VxButtonObj> vertex_button_array, ArrayList<Line> edges_button_array ){
        this.graph.getChildren().addAll(graph_frontend.getChildren());
        this.vertex_button_array = vertex_button_array;
        this.edges_button_array = edges_button_array;
        this.graph_backend = graph_backend;
        this.graph.toFront();

        graph.setDisable(true);
    }


    /**
     * <div  style="font-family:Abadi;">
         * <h2>Function 3: Left click generate vertex</h2>
         * <p style="font-size: 15px;">
         *     Upon left click, if the user  clicked on an empty space
         *     within the graphing area, generate a vertex.
         *     The system would call the createVertex(e) function in order to create a
         *     vertex frontend and backend.
         *     The function also restricts the vertex placement to be only within the graph pane itself.
         * </p>
         *
         * <br>
         * <small>onGraphPressed(e) does not generate a vertex if the clicked area has another
         *      vertex, edge, and circle
         * </small>
     * </div>
     */

    public void onGraphPressed(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            Node clickedNode = (Node) mouseEvent.getTarget();
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            boolean withinGraph = mouseX >= 20 && mouseX <= graph.getWidth() && mouseY >= 20 && mouseY <= graph.getHeight();
            if (!(clickedNode instanceof Button || clickedNode instanceof Line || clickedNode instanceof Circle)
                && withinGraph) {
                graph.getChildren().add(createVertex(mouseEvent));
            }
        }
    }

    /**
     * <div  style="font-family:Abadi;">
     * <h2>Function 4: Create Vertex Function</h2>
     * <strong>This function runs when you create a vertex in the graph pane</strong>
     * <ul>
     *     <li>
     *         <small>FUNCTION 4.1</small>
     *         <p style="font-size: 15px;">
     *             {@link VxButtonObj} class inherits all the components of the Button class.
     *             vertex_button is an object that were styled and styles individually using css
     *         </p>
     *     </li>
     *     <li>
     *         <small>FUNCTION 4.2</small>
     *         <p style="font-size: 15px;">
     *             Vertices are case sensitive and follows the naming incrementation within the ascii table.
     *             The names consist of alphanumeric characters, with the exception of ASCII value 91-96
     *         </p>
     *     </li>
     *     <li>
     *         <small>FUNCTION 4.3</small>
     *         <p style="font-size: 15px;">
     *             Allows the vertex to be draggable by applying drag function individually.
     *             Also implements highlight configuration in order to highlight a selected vertices to form an edge
     *             using an edge connector function.
     *         </p>
     *         <small>Double click in order to highlight a vertex</small>
     *     </li>
     *     <li>
     *         <small>FUNCTION 4.4</small>
     *         <p style="font-size: 15px;">
     *             Deletes the vertex and all connected edges. Fixes the highlight config if a highlighted vertex would be deleted
     *             . First, remove the edges in the graph pane, followed by removing the vertex visually. Then remove the edges through the backend.
     *             In the backend, the vertex is also removed along with its data. Finally, it clears the remaining initialization in the frontend.
     *         </p>
     *         <small>Press the 'DEL' to delete a vertex</small>
     *     </li>
     * </ul>
     * </div>


     */
    private Node createVertex(MouseEvent mouseEvent) {
        // ==== FUNCTION 4.1 ====
        VxButtonObj vertex_button = new VxButtonObj();
        vertex_button.setLayoutX(mouseEvent.getX());
        vertex_button.setLayoutY(mouseEvent.getY());
        vertex_button.getStyleClass().add("UIvertex");
        vertex_button.translateXProperty().bind(vertex_button.widthProperty().divide(-2));
        vertex_button.translateYProperty().bind(vertex_button.heightProperty().divide(-2));

        // ==== FUNCTION 4.2 ====
        if (vertex_button_array.isEmpty()){ // if there are no vertices, create one with its name as A.
            vertex_button.setText("A");
        } else if (!deleted_vertex_names.isEmpty()) {
            vertex_button.setText(String.valueOf(deleted_vertex_names.getFirst()));
            deleted_vertex_names.removeFirst();
        } else { // else if there are already vertices, increment it to other letters.
            char max = vertex_names.getFirst();
            for (char cr : vertex_names)
                max = (max < cr) ? cr : max;
            max++;
            vertex_button.setText(
                (max == '[')
                    ? String.valueOf(max + 6)
                    : String.valueOf(max)
            );
        }
        vertex_names.add(vertex_button.getText().charAt(0));

        // ==== FUNCTION 4.3 ====
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

        // ==== FUNCTION 4.4 ====
        // DELETE THE VERTEX // ALSO DELETE ITS EDGES
        vertex_button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                String style = vertex_button.getStyle();

                // If highlighted vertex is about to be deleted, fix the highlight counter
                if (style.contains("-fx-background-color: red;")){
                    vertex_button.setStyle(null);
                    color_counter -= 1;
                }

                Iterator<Line> it = edges_button_array.iterator();
                while (it.hasNext()){
                    Line tmp = it.next();
                    /* this if statement visually removes the edges if the
                        id of one of the lines is present on the id of the vertex
                    */
                    if (tmp.getId().contains(vertex_button.getText())) {
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
                //graph_backend.print();
                deleted_vertex_names.add(vertex_button.getText().charAt(0));
                for (int i =0; i < vertex_names.size(); i++) {
                    if (vertex_names.get(i) == vertex_button.getText().charAt(0)){
                        vertex_names.remove(i);
                        break;
                    }
                }
            }
        });

        // ADD TO BACKEND
        Vertex v = graph_backend.addVertex(vertex_button.getText());
        vertex_button.setVertexObj(v);
        //graph_backend.addVertex(vertex_button.getText());
        return vertex_button;
    }

/**
 * <div  style="font-family:Abadi;">
 *     <h2>Function 5: Create Edge Function</h2>
 *     <strong>
 *         {@link VxButtonObj} v0 and {@link VxButtonObj} v1 are two vertices that were highlighted.
 *         These two vertices forms an edge through the following process:
 *     </strong>
 *     <ul>
 *         <li>
 *             <small>FUNCTION 5.1</small>
 *             <p style="font-size: 15px;">
 *                 The edge name are the letters of two vertices that are selected.
 *                 The function would have to validate if there is an already existing edge before proceeding.
 *             </p>
 *         </li>
 *         <li>
 *             <small>FUNCTION 5.2</small>
 *             <p style="font-size: 15px;">
 *                 Finds the point location of the two vertices and designate it as two endpoints to form a line.
 *                 The line is then given a function for edge configuration.
 *                 Edge configuration allows the edge to be removed.
 *                 The line are also stored into the graph_backend in order to be used as an edge data.
 *             </p>
 *             <small>Double click a line to remove the edge.</small>
 *         </li>
 *     </ul>
 * </div>
 */
    // CREATE AN EDGE BETWEEN TWO VERTEX
    private void createEdge(VxButtonObj v0, VxButtonObj v1) {
        // ==== FUNCTION 5.1 ====
        // Coordinate of vertex v0 (A) and v1(B)
        String edgename = v0.getText()+v1.getText();
        //check if edge already exist between v0 and v1
        boolean has_duplicate = false;
        for (Line tmp: edges_button_array){
            String s_tmp = tmp.getId();
            if (s_tmp.equals(edgename)){
                System.out.println("Edge already exist!");
                has_duplicate = true;
            }
        }

        // ==== FUNCTION 5.2 ====
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
    /**
     * <div  style="font-family:Abadi;">
     *     <h2>Function 6: Set dragged vertices to front</h2>
     *     <strong>
     *         Visual fix such that dragged vertex are always on the front position among other vertices.
     *     </strong>
     * </div>
     */
    // visual fix for vertex, dont mind
    private void DetectDrag(MouseEvent e, VxButtonObj vertex) {
        vertex.toFront();
    }

    /**
     * <div  style="font-family:Abadi;">
     *     <h2>Function 7: Vertices and edges drag configuration</h2>
     *     <strong>
     *         {@link VxButtonObj} vertex and {@link MouseEvent} e are the vertex that is being dragged and the
     *         mouse that is dragging that vertex.
     *     </strong>
     *     <ul>
     *         <li>
     *             <small>FUNCTION 7.1</small>
     *             <p style="font-size: 15px;">
     *                 Sets the location of the vertex according to the location of the Mouse Event.
     *             </p>
     *         </li>
     *         <li>
     *             <small>FUNCTION 7.2</small>
     *             <p style="font-size: 15px;">
     *                 Simultaneously, in the first for loop, finds the edges that is associated with
     *                 the dragged vertex (e.g: Dragged vertex A = AB, AC, AF, AD).
     *                 The inner loop finds the corresponding edge within all the line in the frontend.
     *                 Once found, determine which of the two end points of the line are being moved and adjust its location.
     *             </p>
     *         </li>
     *     </ul>
     * </div>
     */
    // WHEN THE VERTEX IS DRAGGED
    private void WhenDragged(MouseEvent e, VxButtonObj vertex) {
        //FUNCTION 7.1
        // drag the button or the vertex along the position of the mouse
        double newX = vertex.getLayoutX() + e.getX() + vertex.getTranslateX();
        double newY = vertex.getLayoutY() + e.getY() + vertex.getTranslateY();
        if (newX >= 20 && newX <= 600 && newY >= 20 && newY <= 390){
            vertex.setLayoutX(newX);
            vertex.setLayoutY(newY);

            // ==== FUNCTION 7.2 ====
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

    }
    /**
     * <div  style="font-family:Abadi;">
     *     <h2>Function 8: Main Controller Initialization</h2>
     *     <strong>
     *         Sets animation on the side menu.
     *     </strong>
     * </div>
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This is just when you click that power off button
        exit.setOnMouseClicked(event -> System.exit(0));
        MenuBack.setVisible(false);

        slider.setTranslateX(180);

        Menu.setOnMouseClicked(event -> {
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
    /**
     * <div  style="font-family:Abadi;">
     *     <h2>Function 9: Hamiltonian Circuit Scene</h2>
     *     <strong>
     *         Calls the hamiltonian circuit from another scene, transfers the frontend graph data
     *     </strong>
     * </div>
     */
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

    /**
     * <div  style="font-family:Abadi;">
     *     <h2>Function 10: Eulerian Circuit Scene</h2>
     *     <strong>
     *         Calls the Eulerian circuit from another scene, transfers the frontend graph data
     *     </strong>
     * </div>
     */
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

    /**
     * <div  style="font-family:Abadi;">
     *     <h2>Function 11: Hamiltonian Path Scene</h2>
     *     <strong>
     *         Calls the hamiltonian path from another scene, transfers the frontend graph data
     *     </strong>
     * </div>
     */
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

    /**
     * <div  style="font-family:Abadi;">
     *     <h2>Function 12: About Scene</h2>
     *     <strong>
     *         Calls the about scene in the side menu, contains project information.
     *     </strong>
     * </div>
     */
    // CALL ABOUT
    public void AboutScene (ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
        root = fxmlLoader.load();

        Stage old_stage = (Stage) About.getScene().getWindow();
        about = fxmlLoader.getController();
        about.setPrevious_stage(old_stage);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();


        //''back button''
        old_stage.hide();
    }

    /**
     * <div  style="font-family:Abadi;">
     *     <h2>Function 13: Instruction Scene</h2>
     *     <strong>
     *         Calls the instruction from another scene, contains instruction when using the application
     *     </strong>
     * </div>
     */
    // CALL INSTRUCTION
    public void InstructionScene (ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Instruction.fxml"));
        root = fxmlLoader.load();

        Stage old_stage = (Stage) About.getScene().getWindow();
        instruction = fxmlLoader.getController();
        instruction.setPrevious_stage(old_stage);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        //''back button''
        old_stage.hide();
    }

    // TO EXTRACT THE OUTCOME
    @FXML
    void implementDataModel(ActionEvent event) {
        // if a path/circuit is clicked previously...
        dataModel = showHamilCircuit.getDataModel();
        //dataModel  contains all vertex that is the hamiltonian path.
    }


    Double x = 0.0, y = 0.0;
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