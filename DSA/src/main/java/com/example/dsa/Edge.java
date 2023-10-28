package com.example.dsa;

public class Edge {


    private Vertex from, to;
    private Integer weight;


    public Edge (Vertex source, Vertex destination, Integer weight) {
        this.from    = source;
        this.to = destination;
        this.weight  = weight;
    }

    public Vertex getStartV() {
        return from;
    }

    public Vertex getEndV() {
        return to;
    }


    public Integer getWeight() {
        return weight;
    }



}
