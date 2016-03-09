package com.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by CheokHo on 08/03/2016.
 */
public class NewNodeGUI implements ActionListener {

    private boolean isInitial;
    private boolean isAccepting;
    private TopLevelGUI topLevelGUI;
    private static int x;
    private static int y;


    public NewNodeGUI (boolean isInitial, boolean isAccepting, TopLevelGUI topLevelGUI) {
        this.isAccepting=isAccepting;
        this.isInitial=isInitial;
        this.topLevelGUI=topLevelGUI;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        createGUI();

    }

    public void createGUI() {
        String nodeType = null;
        if (isInitial && !isAccepting) {
            nodeType="initial";
        } else if (isAccepting && !isInitial) {
            nodeType="accepting";
        } else if (isAccepting && isInitial) {
            nodeType="accepting and initial";
        } else if (!isInitial && !isAccepting) {
            nodeType="normal";
        }
        //setTitle("Create new " + nodeType + " node");
        String nodeName = (String)JOptionPane.showInputDialog(new JDialog(), "Specify new node(s) to create. If you want to create multiple nodes, separate them with a space", "Create new "+ nodeType+" node", JOptionPane.PLAIN_MESSAGE, null, null, null);
        boolean hasDupes = false;
        boolean hasInitial = false;
        if (nodeName != null && !nodeName.equals("")) {
            if (nodeType.equals("initial") || nodeType.equals("accepting and initial")) {
                if (!nodeName.matches("[^\\s]")) {
                    hasInitial = true;
                }
                for (Node n: topLevelGUI.getNodeArray()) {
                    if (n.isInitial()) {
                        hasInitial = true;
                        break;
                    }
                }

            }
            ArrayList<String> newNodeArray= new ArrayList(Arrays.asList(nodeName.split("\\s+")));
            for (Node n1: topLevelGUI.getNodeArray()) {
                for (String n2: newNodeArray) {
                    if (n1.toString().equals(n2)) {
                        hasDupes = true;
                        break;
                    }
                }
            }
            if (hasDupes) {
                JOptionPane.showMessageDialog(new JDialog(), "You already have this/these node specified in your graph.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (hasInitial==true) {
                JOptionPane.showMessageDialog(new JDialog(), "Only one initial state can be specified.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int temp=0;
                for (String s: newNodeArray) {
                    if (nodeType.equals("initial")) {
                        topLevelGUI.createNode(x+temp, y+temp, s, false, true);
                    } else if (nodeType.equals("accepting")) {
                        topLevelGUI.createNode(x+temp, y+temp, s, true, false);
                    } else if (nodeType.equals("accepting and initial")) {
                        topLevelGUI.createNode(x+temp, y+temp, s, true, true);
                    } else if (nodeType.equals("normal")) {
                        topLevelGUI.createNode(x+temp, y+temp, s, false, false);
                    }
                    temp=temp+50;
                }

            }
        }
        System.out.println("Current node array(non delete): " + topLevelGUI.getNodeArray().toString());

    }
    public static void setCoord(int x1, int y1){
        x = x1;
        y = y1;
    }
}
