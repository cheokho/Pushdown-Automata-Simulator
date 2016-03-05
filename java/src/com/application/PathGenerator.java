package com.application;

import java.util.ArrayList;

/**
 * Created by CheokHo on 03/03/2016.
 */
public class PathGenerator {

    StringBuilder path;
    ArrayList<String> stackArray;

    public PathGenerator() {
    }

    public void setStringBuilder (StringBuilder path) {
        this.path=path;
    }

    public StringBuilder getPath() {
        return path;
    }

    public ArrayList<String> getStackArray() {
        return stackArray;
    }

    public void setStackArray (ArrayList<String> stackArray) { this.stackArray = stackArray; }
}


