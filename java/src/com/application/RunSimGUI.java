package com.application;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by CheokHo on 18/02/2016.
 */
public class RunSimGUI {

    private String input;
    private String restrictor;
    private Component component;
    private ArrayList<String> inputArray;

    public RunSimGUI(Component component, ArrayList<String> inputArray) {
        this.component=component;
        this.inputArray=inputArray;
    }

    public void showRunSimGUI() {

        restrictor = String.join("", inputArray);
        input = (String) JOptionPane.showInputDialog(component, "Please enter an input for the simulation.", "Run simulation", JOptionPane.QUESTION_MESSAGE);
    }

    public String getInput() {
        if (input !=null) {
            if (!input.matches("[" + restrictor + "]*")) {
                JOptionPane.showMessageDialog(component, "Your input: \"" + input + "\" must only use characters from your alphabet: " + inputArray.toString() + ".\nPlease try again.", "Error", JOptionPane.ERROR_MESSAGE);
                showRunSimGUI();
            }
        }
        return input;
    }
}
