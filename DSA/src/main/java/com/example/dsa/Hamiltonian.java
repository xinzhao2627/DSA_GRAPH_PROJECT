package com.example.dsa;
import java.util.ArrayList;

public class Hamiltonian {

    private int numberOfVertex;
    private ArrayList<String> hamiltonianPath;
    private ArrayList<ArrayList<String>> adjacencyVertex;
    private ArrayList<String> hamilPathEdge;
    private ArrayList<String> hamilCircuitEdge;
    private String startingVertex;
    private String endVertex;
    private String letterVertex;

    public Hamiltonian(ArrayList<Vertex> vertex){
        this.adjacencyVertex = new ArrayList<>();

        String s = "";//this will be used to determine the index of the adjacency vertex in the arraylist

        //store the data (edges of every vertex) to the arraylist
        int i = 0;

        for(Vertex v : vertex) {
            s += v.getData();
            this.adjacencyVertex.add(new ArrayList<>());

            for(Edge e : v.getEdges()) {
                this.adjacencyVertex.get(i).add(e.getEndV().getData());
            }
            i++;
        }

        this.letterVertex = s;
        this.numberOfVertex = this.adjacencyVertex.size();
        this.startingVertex = startingVertex;
        this.endVertex = endVertex;
    }

    public ArrayList<String> getHamilCircuitEdge() {
        return this.hamilCircuitEdge;
    }

    public ArrayList<String> getHamilPathEdge() {
        return this.hamilPathEdge;
    }

    public Boolean getHamilCircuit(String startingVertex){

        this.startingVertex = startingVertex;

        this.hamiltonianPath = new ArrayList<>();

        if(findHamilCircuitSolution(startingVertex)) {
            hamilCircuitEdge = new ArrayList<>();

            System.out.print("Hamiltonian Circuit: ");

            System.out.print(hamiltonianPath.get(0));

            for (int i = 1; i < hamiltonianPath.size(); i++){
                String edgeStr = hamiltonianPath.get(i - 1) + hamiltonianPath.get(i);
                hamilCircuitEdge.add(edgeStr);

                System.out.print(" --> " + hamiltonianPath.get(i));
            }
            System.out.println();
            return true;
        }
        else{
            System.out.println("No Solution.");
            return false;
        }
    }

    public Boolean getHamilPath(String startingVertex, String endVertex){
        this.startingVertex = startingVertex;
        this.endVertex = endVertex;

        this.hamiltonianPath = new ArrayList<>();

        if(findHamilPathSolution(startingVertex)) {
            hamilPathEdge = new ArrayList<>();

            System.out.print("Hamiltonian Path: ");

            System.out.print(hamiltonianPath.get(0));

            for (int i = 1; i < hamiltonianPath.size(); i++){
                String edgeStr = hamiltonianPath.get(i - 1) + hamiltonianPath.get(i);
                hamilPathEdge.add(edgeStr);

                System.out.print(" --> " + hamiltonianPath.get(i));
            }
            System.out.println();
            return true;
        }
        else{
            System.out.println("No Solution.");
            return false;
        }
    }

    private boolean findHamilCircuitSolution(String currentVertex) {

        //checks if the last vertex is connected to the first vertex (circuit)
        if(hamiltonianPath.size() == numberOfVertex){
            if(adjacencyVertex.get(letterVertex.indexOf(hamiltonianPath.get(hamiltonianPath.size() - 1))).contains(hamiltonianPath.get(0))){
                hamiltonianPath.add(hamiltonianPath.get(0));
                return true;
            }else{
                return false;
            }
        }

        //if the current vertex is still not in the path
        if(!hamiltonianPath.contains(currentVertex)){

            //if the current vertex has only 1 edge
            if(adjacencyVertex.get(letterVertex.indexOf(currentVertex)).size() == 1){
                return false;
            }

            hamiltonianPath.add(currentVertex);

            //proceed to the next vertex that is connected to the current vertex and search for path
            for(String str : adjacencyVertex.get(letterVertex.indexOf(currentVertex))){
                if(findHamilCircuitSolution(str)){
                    return true;
                }
            }

            //if fails to find the path (backtrack)
            hamiltonianPath.remove(hamiltonianPath.size() - 1);
        }
        return false;
    }

    private boolean findHamilPathSolution(String currentVertex) {

        //checks if the last vertex is connected to the first vertex (circuit)
        if(hamiltonianPath.size() == numberOfVertex - 1){
            if(adjacencyVertex.get(letterVertex.indexOf(hamiltonianPath.get(hamiltonianPath.size() - 1))).contains(endVertex)){
                hamiltonianPath.add(endVertex);
                return true;
            }else{
                return false;
            }
        }

        //if the current vertex is still not in the path
        if(!hamiltonianPath.contains(currentVertex) && currentVertex != endVertex){

            hamiltonianPath.add(currentVertex);

            //proceed to the next vertex that is connected to the current vertex and search for path
            for(String str : adjacencyVertex.get(letterVertex.indexOf(currentVertex))){
                if(findHamilPathSolution(str)){
                    return true;
                }
            }
            //if fails to find the path (backtrack)
            hamiltonianPath.remove(hamiltonianPath.size() - 1);
        }
        return false;
    }
}
