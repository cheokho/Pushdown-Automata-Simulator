package com.application;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by CheokHo on 16/02/2016.
 */
public class PathFinder {

    GraphNode toNode;
    String currentInput;
    DefaultTableModel model;
    ArrayList<GraphNode> previousNodes = new ArrayList<GraphNode>();


    public PathFinder() {

    }

    public void setToNode(GraphNode toNode) {
        this.toNode = toNode;
    }

    public GraphNode getToNode() {
        return toNode;
    }

    public void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public void addToList(GraphNode node) {
        previousNodes.add(node);
    }

    public ArrayList<GraphNode> getList() {
        return previousNodes;
    }

    public void setTableModel(DefaultTableModel model) {
        this.model=model;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }
}
