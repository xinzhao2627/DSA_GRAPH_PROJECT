package com.example.dsa;

import java.util.*;

public class Vertex {

    private String data;
    private LinkedList< Edge > edges ; //will create a linkedList of Edge object containing vertices
    private LinkedList <Edge > edges_duplicate = new LinkedList<>();
    boolean visited = false;
    boolean oddDeg = false;

    //constructor
    public Vertex(String inputData){
        this.data = inputData;
        this.edges = new LinkedList<>(); //creates a new linkList for every new vertex
    }

    public void creatEdge(Vertex destination, Integer weight){
        Edge edge = new Edge(this, destination, weight);
        this.edges.add(edge);
        this.edges_duplicate.add(edge);
    }

    public void removeEdge(Vertex endVertex) {
        this.edges.removeIf(edge -> edge.getEndV().equals(endVertex));
    }

    public void removeEdgeDup(Vertex endVertex) {
        this.edges_duplicate.removeIf(edge -> edge.getEndV().equals(endVertex));
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

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {

        return visited;
    }

    public LinkedList <Edge> getEdgeDup (){
        return this.edges_duplicate;
    }

    public void restoreEdges (){

        for(Edge e_dup : this.edges_duplicate){

            this.edges.add(e_dup);

        }

    }

    public Boolean getIsOddDeg () {
        return oddDeg;
    }

    public void setIsOddDeg (Boolean deg) {
        this.oddDeg = deg;
    }







}
