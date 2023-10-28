package com.example.dsa;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.LinkedList;


public class Vertex {

    private String data;
    private LinkedList< Edge > edges ; //will create a linkedList of Edge object containing vertices

    //constructor
    public Vertex(String inputData){
        this.data = inputData;
        this.edges = new LinkedList<>(); //creates a new linkList for every new vertex
    }

    public void creatEdge(Vertex destination, Integer weight){
        Edge edge = new Edge(this, destination, weight);
        this.edges.add(edge);
    }

    public void removeEdge(Vertex endVertex) {
        this.edges.removeIf(edge -> edge.getEndV().equals(endVertex));
    }

    public String getData() {
        return this.data;
    }

    public LinkedList<Edge> getEdges(){
        return this.edges;
    }

    public void print(boolean showWeight) {
        String message = "";

        if (this.edges.size() == 0) {
            System.out.println(this.data + " -->");
            return;
        }

        for(int i = 0; i < this.edges.size(); i++) {
            if (i == 0) {
                message += this.edges.get(i).getStartV().data + " -->  ";
            }

            message += this.edges.get(i).getEndV().data;

            if (showWeight) {
                message += " (" + this.edges.get(i).getWeight() + ")";
            }

            if (i != this.edges.size() - 1) {
                message += ", ";
            }
        }
        System.out.println(message);
    }









}