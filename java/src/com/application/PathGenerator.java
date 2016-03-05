package com.application;

import java.util.ArrayList;

/**
 * Created by CheokHo on 03/03/2016.
 */
public class PathGenerator {

    StringBuilder path;
    ArrayList<String> stackArray;
    ArrayList<String> stackOperations;

    public PathGenerator() {
        stackOperations=new ArrayList<String>();
    }

    public void setStringBuilder (StringBuilder path) {
        this.path=path;
    }

    public StringBuilder getPath() {
        return path;
    }

    public String toString() {
        return path.toString();
    }

    public ArrayList<String> getStackArray() {
        return stackArray;
    }

    public void setStackArray (ArrayList<String> stackArray) { this.stackArray = stackArray; }

    public void addToStackOperations(String stackOperation) {
        stackOperations.add(stackOperation);
    }

    public ArrayList<String> getStackOperations() {
        return stackOperations;
    }
}





