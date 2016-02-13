package com.application;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Cheeeky on 13/02/2016.
 */
public class TransitionRuleGUI extends JFrame {

    private ArrayList<String> stackArray;
    private ArrayList<String> inputArray;

    public TransitionRuleGUI(String fromNode, String toNode, ArrayList<String> stackArray, ArrayList<String> inputArray) {
        super("Transition rule from '"+fromNode+"' to '"+toNode+"'.");
        this.stackArray=stackArray;
        this.inputArray=inputArray;
    }
}
