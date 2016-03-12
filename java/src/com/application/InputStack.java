package com.application;

import java.util.ArrayList;

/**
 * Created by CheokHo on 12/03/2016.
 */
public class InputStack {
    private ArrayList<String> inputArray;
    private ArrayList<String> stackArray;

    public InputStack() {
    }

    public void setInputArray(ArrayList<String> inputArray) {
        this.inputArray=inputArray;
    }

    public void setStackArray(ArrayList<String> stackArray) {
        this.stackArray=stackArray;
    }

    public ArrayList<String> getInputArray() {
        return inputArray;
    }

    public ArrayList<String> getStackArray() {
        return stackArray;
    }
}
