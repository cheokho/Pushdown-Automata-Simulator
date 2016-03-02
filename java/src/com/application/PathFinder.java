package com.application;

import javafx.scene.control.Tab;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * Created by CheokHo on 16/02/2016.
 */
public class PathFinder {

    Node toNode;
    String currentInput;
    DefaultTableModel model;
    ArrayList<Node> previousNodes = new ArrayList<Node>();


    public PathFinder() {

    }

    public void setToNode(Node toNode) {
        this.toNode = toNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public void addToList(Node node) {
        previousNodes.add(node);
    }

    public ArrayList<Node> getList() {
        return previousNodes;
    }

    public void setTableModel(DefaultTableModel model) {
        this.model=model;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }
}
