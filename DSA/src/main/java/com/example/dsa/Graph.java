package com.example.dsa;

import java.util.*;

public class Graph {

    //we can treat the graph as an arrayList consisting of LinkedList with each vertex having it's own Linkedlist
    //the linked list will contain a vertex head and it's adjacent vertex/vertices
    //each linked list will be stored inside an arrayList
    //by doing so, we can easily identify which vertex has a connection with other and therefore has an adge


    private ArrayList<Vertex> vertices;
    private boolean isWeighted;
    private boolean isDirected;
    private ArrayList<String> eulerEdges = new ArrayList<>(); //stores the euler edges

    public Graph(boolean inputIsWeighted, boolean inputIsDirected) {
        this.vertices = new ArrayList<Vertex>();
        this.isWeighted = inputIsWeighted;
        this.isDirected = inputIsDirected;
    }

    public Vertex addVertex(String data) {
        Vertex newVertex = new Vertex(data);
        this.vertices.add(newVertex);
        return newVertex;
    }

    public void addEdge(Vertex vertex1, Vertex vertex2, Integer weight) {
        if (!this.isWeighted) {
            weight = null;
        }
        vertex1.creatEdge(vertex2, weight);
        if (!this.isDirected) {
            vertex2.creatEdge(vertex1, weight);
        }
    }

    public void removeEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.removeEdge(vertex2);
        if (!this.isDirected) {
            vertex2.removeEdge(vertex1);
        }
    }

    public void removeVertex(Vertex vertex) {
        this.vertices.remove(vertex);
    }

    public ArrayList<Vertex> getVertices() {
        return this.vertices;
    }

    public boolean isWeighted() {
        return this.isWeighted;
    }

    public boolean isDirected() {
        return this.isDirected;
    }

    public Vertex getVertexByValue(String value) {
        for(Vertex v: this.vertices) {
            if (v.getData() == value) {
                return v;
            }
        }

        return null;
    }

    public void print() {
        for(Vertex v: this.vertices) {
            v.print(isWeighted);
        }
    }

    public ArrayList <String> getEulerEdges(){
        return this.eulerEdges;
    }

    public void clearEulerEdges(){
        this.eulerEdges.clear();
    }

    public void restoreEdges() {

        for(Vertex v : this.getVertices()){
            v.restoreEdges();
        }
    }

    //METHODS USED FOR EULER CIRCUITS AND PATHS


    public void myDFS (Vertex startVertex) {

        if(this.getVertices().contains(startVertex)) {

            //initializes the stack for dfs
            Stack <Vertex> vxstack = new Stack<>();
            //am arraylist to store the visited vertices
            ArrayList <Vertex> visited = new ArrayList<>();
            ArrayList <String> visitedData = new ArrayList<>();

            //the stack will begin with the startVertex
            vxstack.push(startVertex);


            //as long as the stack is not empty
            while(!vxstack.isEmpty()) {

                //pops the current vertex
                Vertex current = vxstack.pop();

                //System.out.println("Current/pop: " + current.getData() );

                //if the current is not yet visited...
                if(!current.isVisited()){

                    //put in the arraylist of visited vertices
                    visited.add(current);
                    visitedData.add(current.getData());

                    //set the visited value to true...
                    current.setVisited(true);
                    //System.out.println("isVisited: " + current.isVisited());

                    //check the edges of the vertex
                    for(Edge e: current.getEdges()) {
                        //set as neighbor
                        Vertex neighbor = e.getEndV();
                        //push in the stack
                        vxstack.push(neighbor);
                        //System.out.println("current's adjascent edges: " + neighbor.getData() );
                    }

                }


            }

            //System.out.println("Visited vertices " + visitedData);


        }

    }

    public Boolean isConnected() {

        //find a vertex with non-zero degree
        Vertex starVertex = new Vertex(null);
        for(Vertex v:this.getVertices() ) {

            v.setVisited(false);

            if(v.getEdges().size() > 0) {
                starVertex = v;
                break;
            }

        }

        //System.out.println("Start vertex: " + starVertex.getData());
        myDFS(starVertex);

        boolean connected = false;
        for(Vertex v: this.getVertices()){

            //System.out.println(v.getData() + " " + v.isVisited());

            if(v.isVisited() == true) {
                connected = true;
            } else {
                connected = false;
            }
        }

        //System.out.println("Connected? " + connected);
        return connected;


    }

    public Boolean isEulerian () {

        // Check if all non-zero degree vertices are connected
        if (isConnected() == true){
            //System.out.println("The graph is Eulerian");
            return true;
        }

        // Count vertices with odd degree
        int odd = 0;

        for(Vertex v : this.getVertices()){

            if(v.getEdges().size() % 2 == 1) {
                odd = odd + 1;
            }

        }
        // If count is more than 2, then graph is not Eulerian

        if(odd == 2) {
            //System.out.println("The graph is Semi-Eulerian");
            return true;
        } else {
            return false;
        }


    }

    public void printEulerPath (Vertex start) {

        if(isEulerian()){

            //will use Fleury's Algorithm
            int odd = 0;
            Vertex start2 = new Vertex(null);

            //Find a vertex with an odd degree
            for(Vertex v : this.getVertices()){
                if(v.getEdges().size() % 2 == 1) {
                    start2 = v;
                    odd = odd + 1;
                }
            }

            if(odd == 2) {
                System.out.println("The graph is only semi-eulerian so we will start with an odd edge instead");
                printEulerUtil(start2);

            } else{
                System.out.println("Output ");
                printEulerUtil(start);
            }

            System.out.println();

        } else {
            System.out.println("The graph is not Eulerian");
        }
    }

    public int dfsCount (Vertex startVertex) {

        //System.out.println("\n" + "START DFS COUNT: " + "\n");

        //initializes the stack for dfs
        Stack <Vertex> vxstack = new Stack<>();
        //am arraylist to store the visited vertices
        ArrayList <Vertex> visited = new ArrayList<>();
        ArrayList <String> visitedData = new ArrayList<>();

        if(this.getVertices().contains(startVertex)) {

            //the stack will begin with the startVertex
            vxstack.push(startVertex);


            //as long as the stack is not empty
            while(!vxstack.isEmpty()) {

                //pops the current vertex
                Vertex current = vxstack.pop();

                //System.out.println("Current/pop: " + current.getData() );

                //if the current is not yet visited...
                if(!current.isVisited()){

                    //put in the arraylist of visited vertices
                    visited.add(current);
                    visitedData.add(current.getData());

                    //set the visited value to true...
                    current.setVisited(true);
                    //System.out.println("isVisited: " + current.isVisited());

                    //check the edges of the vertex
                    for(Edge e: current.getEdges()) {
                        //set as neighbor
                        Vertex neighbor = e.getEndV();
                        //push in the stack
                        vxstack.push(neighbor);
                        //System.out.println("current's adjascent edges: " + neighbor.getData() );
                    }

                }


            }

            //System.out.println("Visited vertices " + visitedData);
            //System.out.println("Count of Visited Vertices: " + visited.size());


        } //System.out.println(" END OF DFS COUNT: " + "\n");
        return visited.size();



    }

    public boolean isValidEdge (Vertex start, Vertex end) {

        //System.out.println("\n" + "START OF isValideEdge method");
        //System.out.println("Checking if valid: " + start.getData() + end.getData());
        //System.out.println("Current graph: ");
        //this.print();
        //System.out.println("\n");

        // The edge start-end is valid in one of the
        // following two cases:

        // 1) If end is the only adjacent vertex of start
        // ie size of adjacent vertex list is 1

        //System.out.println("Size of starting vertex: " + start.getEdges().size());
        if (start.getEdges().size() == 1) {
            return true;
        }

        // 2) If there are multiple adjacents, then
        // u-v is not a bridge Do following steps
        // to check if u-v is a bridge
        // 2.a) count of vertices reachable from u
        int count1 = dfsCount(start);
        //System.out.println("first count: " + count1) ;

        for(Vertex v : this.getVertices()){
            v.setVisited(false);
        }

        // 2.b) Remove edge (u, v) and after removing
        // the edge, count vertices reachable from u
        start.removeEdge(end);

        //System.out.println("\n" + "edge removed: " + start.getData() + end.getData());
        //this.print();
        //System.out.println("    ");


        int count2 = dfsCount(start);
        //System.out.println("second count: " + count2) ;

        // 2.c) Add the edge back to the graph
        start.creatEdge(end, null);

        for(Vertex v : this.getVertices()){
            v.setVisited(false);
        }

        //System.out.println("\n" +"Restored graph: ");
        //this.print();
        //System.out.println("    ");

        //System.out.println("END OF isValideEdge method" + "\n");

        return (count1 > count2) ? false : true;

    }

    public void printEulerUtil(Vertex start) {

        Edge edge = new Edge(null, null, null);

        for(int i = 0; i < start.getEdges().size(); i++) { //access the vertex

            edge = start.getEdges().get(i);


            if(isValidEdge(start, edge.getEndV()) && edge.isTraversed() == false){

                System.out.print(start.getData() + "->" + edge.getEndV().getData() + " -> ");
                String strEdge = start.getData() + edge.getEndV().getData();
                eulerEdges.add(strEdge); // add in the arraylist of euler edges
                //This edge is used so remove it now
                removeEdge(start, edge.getEndV());
                printEulerUtil(edge.getEndV());
            }


        }
    }


    //for testing
    public void printEulerEdge() {

        System.out.println("Euler Edges: ");

        for(int i = 0; i < this.eulerEdges.size(); i++) {
            System.out.print(eulerEdges.get(i) + " -> ");
        }

    }





}
