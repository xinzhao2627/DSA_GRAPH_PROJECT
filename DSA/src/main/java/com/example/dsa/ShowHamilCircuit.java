package com.example.dsa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowHamilCircuit implements Initializable {
    @FXML
    public Button btn_enter;
    @FXML
    public Button btn_exit;
    @FXML
    private TextField start_textfield;
    @FXML
    public Label fromtoprompt;
    @FXML
    AnchorPane HamilCircMain;

    @FXML
    private Button next_btn;
    @FXML
    private Label start_label;
    @FXML
    private Label vertex_label;

    @FXML
    private AnchorPane graph_options;
    AnchorPane graph_dupe;
    ArrayList<VxButtonObj> vertex_button_array;
    ArrayList<Line> edges_button_array;
    Graph graph_backend;
    DataModel dataModel;
    Hamiltonian hamiltonian;
    int counter = 0;


    public void setHamilCircData(ArrayList<VxButtonObj> vertex_button_array, Graph graph_backend, AnchorPane graph_frontend, ArrayList<Line> edges_button_array){
        graph_dupe = graph_frontend;
        for (Line l_tmp: edges_button_array){
            if (l_tmp.isVisible()){
                l_tmp.setVisible(false);
            }
            if (!l_tmp.isDisabled()){
                l_tmp.setDisable(true);
            }
        }
        for (VxButtonObj v_tmp: vertex_button_array){
            if (!v_tmp.isDisabled()){
                v_tmp.setDisable(true);
                v_tmp.setStyle("-fx-opacity: 1");
            }
        }

        next_btn.setVisible(false);
        // check the starting and ending vertex
        String v_max = "A";
        String v_min = "A";
        for (VxButtonObj vertex_tmp: vertex_button_array){
            if (vertex_tmp.getText().charAt(0) > v_max.charAt(0)){
                v_max = vertex_tmp.getText();
            } else if (v_min.charAt(0) < vertex_tmp.getText().charAt(0) ) {
                v_min = vertex_tmp.getText();
            }
        }
        String ini_path = "Select Hamiltonian \n Circuit from\n " + v_min + " to " + v_max;
        fromtoprompt.setText(ini_path);

        this.vertex_button_array = new ArrayList<VxButtonObj>(vertex_button_array);
        this.edges_button_array = new ArrayList<Line>(edges_button_array);
        this.graph_backend = graph_backend;
    }


    // EXIT/CLOSE BUTTON
    public void exit() throws IOException {
        Stage stage = (Stage) btn_exit.getScene().getWindow();

        System.out.println("FINAL GRAPH BACKEND");
        graph_backend.print();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        HelloController helloController;
        helloController = fxmlLoader.getController();

        for (Node node: graph_options.getChildren()){
            node.setVisible(true);
        }



        helloController.setHelloControllerData(graph_options, graph_backend, vertex_button_array, edges_button_array);

        Stage controller_stage = new Stage();
        Scene scene = new Scene(root);
        controller_stage.setScene(scene);
        controller_stage.initStyle(StageStyle.UNDECORATED);
        controller_stage.show();
        //''back button''
        stage.close();


    }

    // when you click enter
    public void enter(MouseEvent mouseEvent) {
        try {
            // ERROR VARIABLE
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Vertex Input");
            errorAlert.setContentText("Invalid Vertex Input");

            // GET TEXT IN THE TEXTFIELD
            String start_vertex = start_textfield.getText();

            if (start_vertex.isEmpty()){
                // ERROR UI
                errorAlert.showAndWait();
            } else {

                //DONT MIND, JUST ERROR CHECKING
                String tmp_start = "";
                for (VxButtonObj v_temp : vertex_button_array){
                    if (start_vertex.equals(v_temp.getText())){
                        tmp_start = v_temp.getText();
                    }
                }

                if (tmp_start.isEmpty()){
                    // ERROR UI
                    errorAlert.showAndWait();
                } else { // if there is no error
                    hamiltonian = new Hamiltonian(graph_backend.getVertices());
                    Boolean checker = hamiltonian.getHamilCircuit(tmp_start);

                    if (!checker){
                        Alert errorAlert2 = new Alert(Alert.AlertType.ERROR);
                        errorAlert2.setTitle("Error");
                        errorAlert2.setHeaderText("NO SOLUTION.");
                        errorAlert2.setContentText("There is no Hamiltonian Circuit in the graph");
                        errorAlert2.showAndWait();
                    } else {
                        next_btn.setVisible(true);
                        fromtoprompt.setVisible(false);
                        start_textfield.setVisible(false);
                        start_label.setVisible(false);
                        vertex_label.setVisible(false);
                        btn_enter.setVisible(false);

                        // MAIN METHODS FOR HAMILTONIAN PATH/CIRCUIT
                    }
                    graph_options.getChildren().addAll(graph_dupe.getChildren());

                }
            }
        } catch (NullPointerException e){
            //CATCH
            System.out.println(e.getMessage());
        }

    }
    @FXML
    void nextAction(MouseEvent event) {
        try {
            if (counter < hamiltonian.getHamilCircuitEdge().size()){
                System.out.println("Working: " + counter);

                for (Line linetmp: edges_button_array){
                    String v0 = String.valueOf(linetmp.getId().charAt(0));
                    String v1 = String.valueOf(linetmp.getId().charAt(1));
                    System.out.println("Line ID:  " + linetmp.getId());
                    System.out.println("HAMILTONIAN EDGES ID " + hamiltonian.getHamilCircuitEdge().get(counter));

                    if(hamiltonian.getHamilCircuitEdge().get(counter).contains(v0) && hamiltonian.getHamilCircuitEdge().get(counter).contains(v1)){
                        linetmp.setVisible(true);
                        linetmp.toBack();
                        linetmp.setDisable(true);
                        break;
                    }
                }
                counter++;
            }
        } catch (NullPointerException e) {
            Alert errorAlert3 = new Alert(Alert.AlertType.ERROR);
            errorAlert3.setTitle("Error");
            errorAlert3.setHeaderText("No Solution.");
            errorAlert3.setContentText("There is no Hamiltonian Circuit in the graph");
            errorAlert3.showAndWait();
        }
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}