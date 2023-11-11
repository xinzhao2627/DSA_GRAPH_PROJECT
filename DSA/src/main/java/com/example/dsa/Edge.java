package com.example.dsa;


public class Edge {


    private Vertex from, to;
    private Integer weight;
    boolean traversed = false;

    public Edge (Vertex source, Vertex destination, Integer weight) {
        this.from    = source;
        this.to = destination;
        this.weight  = weight;
    }

    public String getEdgeName () {
        return from.getData() + to.getData();
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

    public Boolean isTraversed() {
        return traversed;
    }

    public Boolean setTraverse(Boolean trav){
        return trav;
    }


}
