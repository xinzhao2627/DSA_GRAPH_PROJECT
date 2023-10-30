package com.example.dsa;

import javafx.scene.control.Button;

public class VxButtonObj extends Button {
    private Vertex vertexObj;

    public VxButtonObj (Vertex vertex){
        this.vertexObj = vertex;
    }

    public VxButtonObj (){

    }

    public void setVxObj(Vertex vertex){
        this.vertexObj = vertex;
    }

    public Vertex getVertexObj() {
        return vertexObj;
    }

    public void setVertexObj(Vertex vertexObj) {
        this.vertexObj = vertexObj;
    }



}
