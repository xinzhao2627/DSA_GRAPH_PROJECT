package com.example.dsa;

import java.util.ArrayList;

public class DataModel {
    ArrayList<String> e = new ArrayList<>();
    DataModel(ArrayList<String> e){
        this.e = e;
    }

    public Object getE() {
        return e;
    }
}
